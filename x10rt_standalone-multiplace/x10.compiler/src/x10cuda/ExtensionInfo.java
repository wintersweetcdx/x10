// Licensed Materials - Property of IBM
// (C) Copyright IBM Corporation 2004,2005,2006. All Rights Reserved. 
// Note to U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP  Schedule Contract with IBM Corp. 
//                                                                             
// --------------------------------------------------------------------------- 

package x10cuda;

import polyglot.ast.NodeFactory;
import polyglot.frontend.Compiler;
import polyglot.frontend.Goal;
import polyglot.frontend.Scheduler;
import polyglot.main.Options;
import polyglot.types.TypeSystem;
import polyglot.util.ErrorQueue;
import polyglot.visit.PostCompiled;
import x10.ast.X10NodeFactory_c;
import x10cpp.ExtensionInfo.X10CPPScheduler;
import x10cpp.ast.X10CPPExtFactory_c;
import x10cpp.visit.X10CPPTranslator;
import x10cuda.ast.X10CUDADelFactory_c;
import x10cuda.types.X10CUDATypeSystem_c;
import x10cuda.visit.CUDACodeGenerator;


/**
 * Extension information for x10 extension.
 * @author vj -- Adapted from the Polyglot2 ExtensionsInfo for X10 1.5
 */
public class ExtensionInfo extends x10cpp.ExtensionInfo {

	protected NodeFactory createNodeFactory() {
		return new X10NodeFactory_c(this, new X10CPPExtFactory_c(), new X10CUDADelFactory_c()) { };
	}

    protected TypeSystem createTypeSystem() {
        return new X10CUDATypeSystem_c();
    }

    protected Scheduler createScheduler() {
        return new X10CUDAScheduler(this);
    }

    public static class X10CUDAScheduler extends X10CPPScheduler {
        protected X10CUDAScheduler(ExtensionInfo extInfo) {
            super(extInfo);
        }
        protected Goal PostCompiled() {
            return new PostCompiled(extInfo) {
                protected boolean invokePostCompiler(Options options, Compiler compiler, ErrorQueue eq) {
                    if (System.getProperty("x10.postcompile", "TRUE").equals("FALSE"))
                        return true;
                    // use & to avoid short-circuit
                    return CUDACodeGenerator.postCompile(options, compiler, eq) & X10CPPTranslator.postCompile(options, compiler, eq);
                }
            }.intern(this);
        }
    }
}

// vim:tabstop=4:shiftwidth=4:expandtab