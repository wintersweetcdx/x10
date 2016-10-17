/*
 *  This file is part of the X10 project (http://x10-lang.org).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  (C) Copyright IBM Corporation 2006-2016.
 */

import x10.array.Array;
import x10.array.Array_2;
import x10.util.foreach.Block;
import x10.util.OptionsParser;
import x10.util.Option;
import x10.util.Pair;
import x10.util.Random;

/**
 * A distributed implementation of KMeans clustering.
 *
 * Intra-place concurrency is exposed via Foreach.
 *
 * A PlaceLocalHandle is used to store the local state of
 * the algorithm which consists of the points the place
 * is responsible for assigning to clusters and scratch storage
 * for computing a step of the algorithm by determing which
 * cluster each point is currently assigned to and what the
 * new centroid of that cluster is.
 *
 * For the highest throughput and most scalable implementation of
 * the KMeans algorithm in X10 for Native X10, see KMeans.x10 in the
 * X10 Benchmarks Suite (separate download from x10-lang.org).
 */
public class KMeans {

    /*
     * The local state consists of the points assigned to
     * this place and scratch storage for computing the new
     * cluster centroids based on the current assignment
     * of points to clusters.
     */
    static class LocalState implements x10.io.Unserializable {
        val points:Array_2[Float];
        val clusters:Array_2[Float];
        val clusterCounts:Rail[Int];
        val numPoints:Long;
        val numClusters:Long;
        val dim:Long;

        def this(initPoints:(Place)=>Array_2[Float], dim:Long, numClusters:Long) {
            points = initPoints(here);
            clusters  = new Array_2[Float](numClusters, dim);
            clusterCounts = new Rail[Int](numClusters);
            numPoints = points.numElems_1;
            this.numClusters = numClusters;
            this.dim = dim;
        }

        def localStep(currentClusters:Array_2[Float]) {
            // clear scratch storage to prepare for this step
            clusters.raw().clear();
            clusterCounts.clear();

            // Primary kernel: for every point, determine current closest
            //                 cluster and assign the point to that cluster.
            Block.for(mine:LongRange in 0..(numPoints-1)) {
                val scratchClusters = new Array_2[Float](numClusters, dim);
                val scratchClusterCounts = new Rail[Int](numClusters);
                for (p in mine) {
                    var closest:Long = -1;
                    var closestDist:Float = Float.MAX_VALUE;
                    for (k in 0..(numClusters-1)) {
                        var dist : Float = 0;
                        for (d in 0..(dim-1)) {
                            val tmp = points(p,d) - currentClusters(k,d);
                            dist += tmp * tmp;
                        }
                        if (dist < closestDist) {
                            closestDist = dist;
                            closest = k;
                        }
                    }
                    for (d in 0..(dim-1)) {
                        scratchClusters(closest,d) += points(p,d);
                    }
                    scratchClusterCounts(closest)++;
                }
                atomic {
                    for ([i,j] in scratchClusters.indices()) {
                        clusters(i,j) += scratchClusters(i,j);
                    }
                    for (i in scratchClusterCounts.range()) {
                        clusterCounts(i) += scratchClusterCounts(i);
                    }
                }
            }
        }
    }

    static class KMeansMaster implements x10.io.Unserializable {
        val lsPLH:PlaceLocalHandle[LocalState];
        val currentClusters:Array_2[Float];
        val newClusters:Array_2[Float];
        val newClusterCounts:Rail[Int];
        val pg:PlaceGroup;
        val numClusters:Long;
        val dim:Long;
        val epsilon:Float;
        var converged:Boolean = false;

        def this(lsPLH:PlaceLocalHandle[LocalState], pg:PlaceGroup, epsilon:Float) {
           this.lsPLH = lsPLH;
           this.pg = pg;
           this.epsilon = epsilon;
           this.numClusters = lsPLH().numClusters;
           this.dim = lsPLH().dim;
           currentClusters = new Array_2[Float](numClusters, dim);
           newClusters = new Array_2[Float](numClusters, dim);
           newClusterCounts = new Rail[Int](numClusters);
        }

        def setInitialCentroids() {
            finish {
                for (p in pg) async {
                    val plh = lsPLH; // don't capture this!
                    val tmp = at (p) { new Array_2[Float](plh().numClusters, plh().dim, (i:Long, j:Long) => { plh().points(i,j) }) };
                    atomic {
                        for ([i,j] in currentClusters.indices()) {
                            currentClusters(i,j) += tmp(i,j);
                        }
                    }
                }
            }
            val np = pg.numPlaces();
            for ([i,j] in currentClusters.indices()) {
                currentClusters(i,j) /= np;
            }
        }

        def isFinished() = converged;

        // Perform one global step of the KMeans algorithm
        // by coordinating the localSteps in each place and
        // accumulating the resulting new cluster centroids.
        def step() {
            finish {
                for (p in pg) async {
                    val shadowClusters = this.currentClusters; // avoid capture of KMeansMaster object in at!
                    val shadowPLH = this.lsPLH;                // avoid capture of KMeansMaster object in at!
                    val partialResults = at (p) {
                        val ls = shadowPLH();
                        ls.localStep(shadowClusters);
                        Pair[Array_2[Float],Rail[Int]](ls.clusters, ls.clusterCounts)
                    };
                    atomic {
                        for ([i,j] in newClusters.indices()) {
                            newClusters(i,j) += partialResults.first(i,j);
                        }
                        for (i in newClusterCounts.range()) {
                            newClusterCounts(i) += partialResults.second(i);
                        }
                    }
                }
            }

            // Normalize to compute new cluster centroids
            for (k in 0..(numClusters-1)) {
                if (newClusterCounts(k) > 0) {
                    for (d in 0..(dim-1)) newClusters(k,d) /= newClusterCounts(k);
                }
            }

            // Test for convergence
            var didConverge:Boolean = true;
            for ([i,j] in newClusters.indices()) {
                if (Math.abs(currentClusters(i,j) - newClusters(i,j)) > epsilon) {
                    didConverge = false;
                    break;
                }
            }
            converged = didConverge;

            // Prepare for next iteration
            Array.copy(newClusters, currentClusters);
            newClusters.raw().clear();
            newClusterCounts.clear();
        }
    }

    static def printPoints (clusters:Array_2[Float]) {
        for (d in 0..(clusters.numElems_2-1)) {
            for (k in 0..(clusters.numElems_1-1)) {
                if (k>0)
                    Console.OUT.print(" ");
                Console.OUT.print(clusters(k,d).toString());
            }
            Console.OUT.println();
        }
    }

    static def computeClusters(pg:PlaceGroup, initPoints:(Place)=>Array_2[Float], dim:Long,
                               numClusters:Long, iterations:Long, epsilon:Float, verbose:Boolean):Array_2[Float] {

        // Initialize LocalState in every Place
        val localPLH = PlaceLocalHandle.make[LocalState](pg, ()=>{ new LocalState(initPoints, dim, numClusters) });

        // Initialize algorithm state
        val master = new KMeansMaster(localPLH, pg, epsilon);
        master.setInitialCentroids();

        if (verbose) {
            Console.OUT.println("Initial clusters: ");
            printPoints(master.currentClusters);
        }

        // Perform iterative algorithm
        for (iter in 0..(iterations-1)) {
            master.step();
            if (master.isFinished()) break;

            if (verbose) {
                Console.OUT.println("Iteration: "+iter);
                printPoints(master.currentClusters);
            }
        }

        return master.currentClusters;
    }

    public static def main (args:Rail[String]) {
        val opts = new OptionsParser(args, [
            Option("q","quiet","just print time taken"),
            Option("v","verbose","print out each iteration"),
            Option("h","help","this information")
        ], [
            Option("i","iterations","quit after this many iterations"),
            Option("c","clusters","number of clusters to find"),
            Option("d","dim","number of dimensions"),
            Option("e","epsilon","convergence threshold"),
            Option("n","num","quantity of points")
        ]);
        if (opts.filteredArgs().size!=0L) {
            Console.ERR.println("Unexpected arguments: "+opts.filteredArgs());
            Console.ERR.println("Use -h or --help.");
            System.setExitCode(1n);
            return;
        }
        if (opts("-h")) {
            Console.OUT.println(opts.usage(""));
            return;
        }

        val numClusters = opts("-c",4);
        val numPoints = opts("-n", 2000);
        val iterations = opts("-i",50);
        val dim = opts("-d", 4);
        val epsilon = opts("-e", 1e-3f); // negative epsilon forces i iterations.
        val verbose = opts("-v");

        Console.OUT.println("points: "+numPoints+" clusters: "+numClusters+" dim: "+dim);

        val pg = Place.places();
        val pointsPerPlace = numPoints / pg.size();
        val initPoints = (p:Place) => {
            val rand = new x10.util.Random(p.id);
            val pts = new Array_2[Float](pointsPerPlace, dim, (Long,Long)=> rand.nextFloat());
            pts
        };

        val start = System.nanoTime();
        val clusters = computeClusters(pg, initPoints, dim, numClusters, iterations, epsilon, verbose);
        val stop = System.nanoTime();
        Console.OUT.printf("TOTAL_TIME: %.3f seconds\n", (stop-start)/1e9);

        if (verbose) {
            Console.OUT.println("\nFinal results:");
            printPoints(clusters);
        }
    }
}

// vim: shiftwidth=4:tabstop=4:expandtab
