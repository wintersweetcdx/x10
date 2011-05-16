#ifndef X10_IO_IOEXCEPTION_H
#define X10_IO_IOEXCEPTION_H

#include <x10aux/config.h>
#include <x10aux/RTT.h>

#include <x10/lang/Exception.h>

namespace x10 {

    namespace io {

        class IOException : public x10::lang::Exception {
        public:
            class RTT : public x10aux::RuntimeType { 
                public:
                static RTT* const it; 
            
                virtual void init() {
                    initParents(1,x10aux::getRTT<x10::lang::Exception>());
                }
                
                virtual const char *name() const { return "x10.io.IOException"; }
            };

            virtual const x10aux::RuntimeType *_type() const {
                return x10aux::getRTT<IOException>();
            }

            static x10aux::ref<IOException> _make()
            { return (new (x10aux::alloc<IOException>()) IOException()) ->_constructor(); }

            static x10aux::ref<IOException> _make(x10aux::ref<x10::lang::String> message)
            { return (new (x10aux::alloc<IOException>()) IOException()) ->_constructor(message); }

            static x10aux::ref<IOException> _make(x10aux::ref<Throwable> cause)
            { return (new (x10aux::alloc<IOException>()) IOException()) ->_constructor(cause); }
    
            static x10aux::ref<IOException> _make(x10aux::ref<x10::lang::String> message,
                                                x10aux::ref<Throwable> cause)
            {
                return (new (x10aux::alloc<IOException>()) IOException())
                    ->_constructor(message, cause);
            }

            
            static const x10aux::serialization_id_t _serialization_id;

            virtual void _serialize_id(x10aux::serialization_buffer &buf, x10aux::addr_map &m) {
                buf.write(_serialization_id,m);
            }

            template<class T>
            static x10aux::ref<T> _deserializer(x10aux::serialization_buffer &buf) {
                x10aux::ref<IOException> this_ = new (x10aux::alloc<IOException>()) IOException();
                this_->_deserialize_body(buf);
                return this_;
            }


        };

    }
}


#endif
// vim:tabstop=4:shiftwidth=4:expandtab