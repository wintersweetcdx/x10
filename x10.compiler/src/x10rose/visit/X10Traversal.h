/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class Test */

#ifndef _Included_Test
#define _Included_Test
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     JNI
 * Method:    cactionTest
 * Signature: ()V
 */
JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionTest(JNIEnv *, jclass);

JNIEXPORT void JNICALL
Java_x10rose_visit_JNI_cactionConditionalExpression(JNIEnv *, jclass, jobject); 

JNIEXPORT void JNICALL
Java_x10rose_visit_JNI_cactionConditionalExpressionEnd(JNIEnv *, jclass, jobject);

JNIEXPORT void JNICALL
Java_x10rose_visit_JNI_cactionFalseLiteral(JNIEnv *, jclass, jobject);

JNIEXPORT void JNICALL
Java_x10rose_visit_JNI_cactionTrueLiteral(JNIEnv *, jclass, jobject); 

JNIEXPORT void JNICALL
Java_x10rose_visit_JNI_cactionIfStatement(JNIEnv *, jclass, jobject); 

JNIEXPORT void JNICALL
Java_x10rose_visit_JNI_cactionIfStatementEnd(JNIEnv *, jclass, jboolean, jobject);

JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionAssignment(JNIEnv *, jclass, jobject);
    
JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionAssignmentEnd(JNIEnv *, jclass, jobject);

JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionIntLiteral(JNIEnv *, jclass, jint, jstring, jobject); 

JNIEXPORT void JNICALL
Java_x10rose_visit_JNI_cactionLongLiteral(JNIEnv *, jclass, jlong, jstring, jobject);

JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionPushPackage(JNIEnv *, jclass, jstring, jobject);

JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionImportReference(JNIEnv *, jclass,
                                                        jboolean,
                                                        jstring,
                                                        jstring,
                                                        jstring,
                                                        jboolean,
                                                        jobject);

JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionThisReference(JNIEnv *, jclass, jobject);

JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionSuperReference(JNIEnv *env, jclass, jobject jToken); 

JNIEXPORT void JNICALL
Java_x10rose_visit_JNI_cactionEmptyStatement(JNIEnv *, jclass, jobject);

JNIEXPORT void JNICALL
Java_x10rose_visit_JNI_cactionEmptyStatementEnd(JNIEnv *, jclass, jobject);


JNIEXPORT void JNICALL
Java_x10rose_visit_JNI_cactionConstructorDeclaration(JNIEnv *, jclass, jstring, jint, jobject);

JNIEXPORT void JNICALL
Java_x10rose_visit_JNI_cactionConstructorDeclarationHeader(JNIEnv *, jclass,
                                                                    jstring,
                                                                           jboolean,
                                                                           jboolean,
                                                                           jint,
                                                                           jint,
                                                                           jint,
                                                                           jobject);


JNIEXPORT void JNICALL
Java_x10rose_visit_JNI_cactionConstructorDeclarationEnd(JNIEnv *, jclass, jint, jobject); 

JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionSetupObject(JNIEnv *env, jclass);

JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionInsertClassStart2(JNIEnv *, jclass, jstring, jobject); 

JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionInsertClassStart(JNIEnv *, jclass, jstring, jobject); 

JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionInsertClassEnd(JNIEnv *, jclass, jstring, jobject); 

JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionInsertImportedPackage(JNIEnv *, jclass, jstring, jobject); 

JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionInsertImportedType(JNIEnv *, jclass, jstring, jstring, jobject); 

JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionCompilationUnitList(JNIEnv *, jclass, jint, jobjectArray); 

JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionCompilationUnitDeclaration(JNIEnv *, jclass, jstring, jstring, jobject);

JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionBuildClassSupportStart(JNIEnv *, jclass, jstring, jstring, jboolean, jboolean, jboolean, jboolean, jboolean, jobject);

JNIEXPORT void JNICALL
Java_x10rose_visit_JNI_cactionBuildClassSupportEnd(JNIEnv *, jclass, jstring, jobject); 

JNIEXPORT void JNICALL
Java_x10rose_visit_JNI_cactionTypeDeclaration(JNIEnv *, jclass,
                                              jstring,
                                              jstring,
                                              jboolean,
                                              jboolean,
                                              jboolean,
                                              jboolean,
                                              jboolean,
                                              jboolean,
                                              jboolean,
                                              jboolean,
                                              jboolean,
                                              jboolean);

JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionBlock(JNIEnv *, jclass, jobject);

JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionBlockEnd(JNIEnv *, jclass, jint, jobject); 


JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionReturnStatement(JNIEnv *, jclass, jobject);

JNIEXPORT void JNICALL
Java_x10rose_visit_JNI_cactionReturnStatementEnd(JNIEnv *, jclass, jboolean, jobject);

JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionBinaryExpression(JNIEnv *, jclass, jobject); 

JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionBinaryExpressionEnd(JNIEnv *, jclass, jint, jobject);

JNIEXPORT void JNICALL
Java_x10rose_visit_JNI_cactionMethodDeclaration(JNIEnv *, jclass,
                                                jstring, 
																jint,
																jint,
																jobject,
																jobject,
																jobject);

JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionMethodDeclarationEnd(JNIEnv *v, jclass, jint, jobject);


JNIEXPORT void JNICALL 
Java_x10rose_visit_JNI_cactionTypeReference(JNIEnv *, jclass, 
															jstring, 
															jstring, 
															jobject);


JNIEXPORT void JNICALL Java_x10rose_visit_JNI_cactionCatchArgument(JNIEnv *, jclass, jstring, jobject);


JNIEXPORT void JNICALL Java_x10rose_visit_JNI_cactionArrayTypeReference(JNIEnv *, jclass, jint, jobject);

JNIEXPORT void JNICALL Java_x10rose_visit_JNI_cactionStringLiteral(JNIEnv *, jclass, jstring, jobject); 

JNIEXPORT void JNICALL Java_x10rose_visit_JNI_cactionBreakStatement(JNIEnv *, jclass, jstring, jobject);

JNIEXPORT void JNICALL Java_x10rose_visit_JNI_cactionCaseStatement(JNIEnv *, jclass, jboolean, jobject);

JNIEXPORT void JNICALL Java_x10rose_visit_JNI_cactionCharLiteral(JNIEnv *, jclass, jchar, jobject);

JNIEXPORT void JNICALL Java_x10rose_visit_JNI_cactionConditionalExpressionEnd(JNIEnv *, jclass, jobject); 

JNIEXPORT void JNICALL Java_x10rose_visit_JNI_cactionContinueStatement(JNIEnv *, jclass, jstring, jobject);

JNIEXPORT void JNICALL Java_x10rose_visit_JNI_cactionForStatement(JNIEnv *, jclass, jobject); 

JNIEXPORT void JNICALL Java_x10rose_visit_JNI_cactionIfStatement(JNIEnv *, jclass, jobject);


JNIEXPORT void JNICALL Java_x10rose_visit_JNI_cactionSwitchStatement(JNIEnv *, jclass, jobject);

JNIEXPORT void JNICALL Java_x10rose_visit_JNI_cactionBuildMethodSupportStart(JNIEnv *, jclass, jstring, jint, jobject);

JNIEXPORT void JNICALL Java_x10rose_visit_JNI_cactionBuildMethodSupportEnd(JNIEnv *, jclass, jstring, jint, jboolean, jboolean, jboolean, jint, jint, jboolean, jobject, jobject);

JNIEXPORT void JNICALL Java_x10rose_visit_JNI_cactionBuildArgumentSupport(JNIEnv *, jclass, jstring, jboolean, jboolean, jobject);

JNIEXPORT void JNICALL Java_x10rose_visit_JNI_cactionSingleNameReference(JNIEnv *, jclass, jstring, jstring, jstring, jobject);


#ifdef __cplusplus
}
#endif
#endif
