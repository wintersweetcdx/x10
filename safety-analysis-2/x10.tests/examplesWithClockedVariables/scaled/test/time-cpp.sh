export X10RT_STANDALONE_NUMPLACES=1
args=""
example=""
while [ -n "$1" ]; do
case "$1" in
        -*) args="${args} $1";;
	*) example=($*); break;;
esac
shift
done
if [ -z "$example" ]; then
 example=(AllReduceParallel Pipeline Convolve NQueensPar MontyPiParallel KMeansScalar Histogram MergeSort Stream Prefix UTS IDEA SOR Stencil Series RayTrace LUFact SparseMatMul)
fi


echo "Sno. Application Original(ms) Determinized(ms) Speed-up"
rm out.txt
COUNT=3
WORKERS=4
for ((i = 0; i < ${#example[@]}; i++))
do
	DIFF1=0
		../../../../x10.dist/bin/x10c++ -SAFE_PARALLELIZATION_CHECK=true -x10rt standalone ../${example[$i]}.x10 >> out.txt 
	for ((j = 0; j < $COUNT; j++))
	do
		./a.out $args  >> out.txt
		line=$(tail -n 1 out.txt) #read line < time.txt
		DIFF1=$(($DIFF1+$line))
	done
	DIFF2=0

	../../../../x10.dist/bin/x10c++ -x10rt standalone ../${example[$i]}Orig.x10 >> out.txt 
	for ((j = 0; j < $COUNT; j++))
	do
		./a.out $args >> out.txt
		line=$(tail -n 1 out.txt) #read line < time.txt
		DIFF2=$(($DIFF2+$line))
	done
	echo ""|awk -v sn=$i -v app=${example[$i]} -v n1=$DIFF2 -v n2=$DIFF1 '{print sn+1, app, n1, n2, n1/n2}'
	
done

rm *.java *.class

