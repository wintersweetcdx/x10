#ifndef X10AUX_RTT_H
#define X10AUX_RTT_H

#define DEFINE_RTT(T) \
    DEFINE_SPECIAL_RTT(T::RTT)

#define DEFINE_SPECIAL_RTT(T) \
    T * const T::it = new (x10aux::alloc<T >()) T()

// [DC] can't do RTT macros for generic classes because they would have to be
// variadic to handle the varying number of type parameters


#include <x10aux/config.h>
#include <x10aux/alloc.h>

namespace x10 {
    namespace lang {
        class Object;
    }
}

namespace x10aux {

    template<class T> class ref;

    class RuntimeType {

        public:

        int parentsc;
        const RuntimeType ** parents;

        RuntimeType() : parentsc(-1), parents(0) {
            _RTT_("Creating uninitialised RTT: "<<std::hex<<this<<std::dec);
        }

        bool initialized() { return parentsc>=0; }

        virtual void init() = 0;

        void initParents(int parentsc_, ...);

        virtual ~RuntimeType();

        virtual const char *name() const = 0;

        virtual bool subtypeOf(const RuntimeType * const other) const {
            if (equals(other)) return true; // trivial case
            for (int i = 0; i < parentsc; ++i) {
                if (parents[i]->subtypeOf(other)) return true;
            }
            return false;
        }

        // use "const ref<t> &" here to break circular dependency
        virtual bool instanceOf(const x10aux::ref<x10::lang::Object> &other) const;

        // use "const ref<t> &" here to break circular dependency
        virtual bool concreteInstanceOf(const x10aux::ref<x10::lang::Object> &other) const;

        virtual bool equals(const RuntimeType * const other) const {
            if (other == this) return true;
            return false;
        }

    };

    template<class T> struct RTT_WRAP { static const RuntimeType *_() {
        RuntimeType *it = T::RTT::it;
        if (it == NULL) return NULL;
        if (!it->initialized()) {
            it->init();
        }
        return it;
    } };

    template<class T> struct RTT_WRAP<ref<T> > { static const RuntimeType *_() {
        return RTT_WRAP<T>::_();
    } };

    // this is the function we use to get runtime types from types
    template<class T> const RuntimeType *getRTT() {
        return RTT_WRAP<T>::_();
    }


    // This is different to getRTT because it distinguishes between T and ref<T>
    template<class T> struct TypeName { static const char *_() {
        const RuntimeType *t = getRTT<T>();
        if (t == NULL) return "Uninitialized RTT";
        return t->name();
    } };

    template<class T> struct TypeName<ref<T> > { static const char *_() {
        const RuntimeType *t = getRTT<T>();
        if (t == NULL) return "Uninitialized RTT";
        static const char *with_star = alloc_printf("%s*",t->name());
        return with_star;
    } };

    template<class T> const char *typeName() {
        return TypeName<T>::_();
    }

    void primitive_init(RuntimeType* t);

#define DECLARE_PRIMITIVE_RTT(C,P) \
    class C##Type : public RuntimeType { \
    public: \
        static C##Type * const it; \
        virtual void init() { primitive_init(this); } \
        virtual ~C##Type() { } \
        virtual const char *name() const { return "x10.lang."#C; } \
    }; \
    template<> struct RTT_WRAP<C##Type> { static RuntimeType *_() { \
        return C##Type::it; \
    } }; \
    template<> struct RTT_WRAP<x10_##P> { static RuntimeType *_() { \
        return C##Type::it; \
    } }
#define DEFINE_PRIMITIVE_RTT(C) \
    DEFINE_SPECIAL_RTT(C##Type)

    DECLARE_PRIMITIVE_RTT(Boolean, boolean);
    DECLARE_PRIMITIVE_RTT(Byte, byte);
    DECLARE_PRIMITIVE_RTT(Char, char);
    DECLARE_PRIMITIVE_RTT(Short, short);
    DECLARE_PRIMITIVE_RTT(Int, int);
    DECLARE_PRIMITIVE_RTT(Long, long);
    DECLARE_PRIMITIVE_RTT(Float, float);
    DECLARE_PRIMITIVE_RTT(Double, double);

#undef DECLARE_PRIMITIVE_RTT

    #define TYPENAME(T) x10aux::typeName<T>()
    class place;
    template<> inline const char *typeName<place>() { return "place"; }
    template<> inline const char *typeName<x10_remote_ref_t>() { return "x10_remote_ref_t"; }
    class InitDispatcher;
    template<> inline const char *typeName<InitDispatcher>() { return "InitDispatcher"; }
    template<> inline const char *typeName<void (*)()>() { return "void (*)()"; }
    template<> inline const char *typeName<const void*>() { return "const void *"; }
    template<> inline const char *typeName<char>() { return "char"; }
    template<> inline const char *typeName<const RuntimeType*>() { return "const RuntimeType *"; }

    template<class T> inline x10_boolean instanceof(const x10aux::ref<x10::lang::Object> &v) {
        return x10aux::getRTT<T>()->instanceOf(v);
    }

    template<class T> inline x10_boolean concrete_instanceof(const x10aux::ref<x10::lang::Object> &v) {
        return x10aux::getRTT<T>()->concreteInstanceOf(v);
    }

    template<class T1,class T2> inline x10_boolean subtypeof() {
        return x10aux::getRTT<T1>()->subtypeOf(x10aux::getRTT<T2>());
    }

    template<class T1,class T2> inline x10_boolean sametype() {
        return x10aux::getRTT<T1>()->equals(x10aux::getRTT<T2>());
    }

}

#endif
// vim:tabstop=4:shiftwidth=4:expandtab