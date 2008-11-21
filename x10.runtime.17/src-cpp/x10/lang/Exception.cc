#include <x10aux/config.h>

#include <x10/lang/Exception.h>

using namespace x10::lang;
using namespace x10aux;


void Exception::_serialize_fields(x10aux::serialization_buffer& buf, x10aux::addr_map& m) {
    Throwable::_serialize_fields(buf, m);
}

void Exception::_deserialize_fields(x10aux::serialization_buffer& buf) {
    (void)buf;
}

DEFINE_RTT(Exception);
// vim:tabstop=4:shiftwidth=4:expandtab
