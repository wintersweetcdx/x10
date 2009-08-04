#include <x10aux/config.h>
#include <x10aux/RTT.h>
#include <x10aux/alloc.h>
#include <x10aux/atomic_ops.h>

#include <x10/lang/Object.h>

#include <cstdarg>

using namespace x10aux;
using namespace x10::lang;

bool RuntimeType::subtypeOf(const RuntimeType * const other) const {
    // Checks to try to catch partially initialized RTT objects before we use them.
    assert(typeName != NULL);
    assert(other->typeName != NULL);
    assert(parentsc != 0 || this == x10::lang::Object::getRTT());
    assert(other->parentsc != 0 || other == x10::lang::Object::getRTT());

    if (equals(other)) return true; // trivial case
    for (int i = 0; i < parentsc; ++i) {
        if (parents[i]->subtypeOf(other)) return true;
    }
    return false;
}

bool RuntimeType::instanceOf (const ref<Object> &other) const {
    if (other.isNull())
        return false;
    return other->_type()->subtypeOf(this);
}

bool RuntimeType::concreteInstanceOf (const ref<Object> &other) const {
    if (other.isNull())
        return false;
    return other->_type()->equals(this);
}

void RuntimeType::init(const char* typeName_, int parentsc_, const RuntimeType** parents_,
                       int paramsc_, const RuntimeType** params_, Variance* variances_) {
    typeName = typeName_;
    parentsc = parentsc_;
    paramsc = paramsc_;
    if (parentsc > 0) {
        parents = alloc<const RuntimeType*>(parentsc * sizeof(const RuntimeType*));
        for (int i=0; i<parentsc; i++) {
            parents[i] = parents_[i];
        }
    } else {
        parents = NULL;
    }
    if (paramsc > 0) {
        params = alloc<const RuntimeType*>(paramsc * sizeof(const RuntimeType*));
        variances = alloc<Variance>(paramsc * sizeof(Variance));
        for (int i=0; i<paramsc; i++) {
            params[i] = params_[i];
            variances[i] = variances_[i];
        }
    } else {
        params = NULL;
        variances = NULL;
    }
    x10aux::atomic_ops::store_load_barrier();
}
    
void RuntimeType::initBooleanType() {
    const RuntimeType* parents[1] = {x10::lang::Object::getRTT()};
    BooleanType.init("x10.lang.Boolean", 1, parents, 0, NULL, NULL);
}
void RuntimeType::initByteType() {
    const RuntimeType* parents[1] = {x10::lang::Object::getRTT()};
    ByteType.init("x10.lang.Byte", 1, parents, 0, NULL, NULL);
}
void RuntimeType::initCharType() {
    const RuntimeType* parents[1] = {x10::lang::Object::getRTT()};
    CharType.init("x10.lang.Char", 1, parents, 0, NULL, NULL);
}
void RuntimeType::initShortType() {
    const RuntimeType* parents[1] = {x10::lang::Object::getRTT()};
    ShortType.init("x10.lang.Short", 1, parents, 0, NULL, NULL);
}
void RuntimeType::initIntType() {
    const RuntimeType* parents[1] = {x10::lang::Object::getRTT()};
    IntType.init("x10.lang.Int", 1, parents, 0, NULL, NULL);
}
void RuntimeType::initFloatType() {
    const RuntimeType* parents[1] = {x10::lang::Object::getRTT()};
    FloatType.init("x10.lang.Float", 1, parents, 0, NULL, NULL);
}
void RuntimeType::initLongType() {
    const RuntimeType* parents[1] = {x10::lang::Object::getRTT()};
    LongType.init("x10.lang.Long", 1, parents, 0, NULL, NULL);
}
void RuntimeType::initDoubleType() {
    const RuntimeType* parents[1] = {x10::lang::Object::getRTT()};
    DoubleType.init("x10.lang.Double", 1, parents, 0, NULL, NULL);
}
void RuntimeType::initUByteType() {
    const RuntimeType* parents[1] = {x10::lang::Object::getRTT()};
    UByteType.init("x10.lang.UByte", 1, parents, 0, NULL, NULL);
}
void RuntimeType::initUShortType() {
    const RuntimeType* parents[1] = {x10::lang::Object::getRTT()};
    UShortType.init("x10.lang.UShort", 1, parents, 0, NULL, NULL);
}
void RuntimeType::initUIntType() {
    const RuntimeType* parents[1] = {x10::lang::Object::getRTT()};
    UIntType.init("x10.lang.UInt", 1, parents, 0, NULL, NULL);
}
void RuntimeType::initULongType() {
    const RuntimeType* parents[1] = {x10::lang::Object::getRTT()};
    ULongType.init("x10.lang.ULong", 1, parents, 0, NULL, NULL);
}


RuntimeType RuntimeType::BooleanType;
RuntimeType RuntimeType::ByteType;
RuntimeType RuntimeType::CharType;
RuntimeType RuntimeType::ShortType;
RuntimeType RuntimeType::IntType;
RuntimeType RuntimeType::FloatType;
RuntimeType RuntimeType::LongType;
RuntimeType RuntimeType::DoubleType;
RuntimeType RuntimeType::UByteType;
RuntimeType RuntimeType::UShortType;
RuntimeType RuntimeType::UIntType;
RuntimeType RuntimeType::ULongType;

// vim:tabstop=4:shiftwidth=4:expandtab
