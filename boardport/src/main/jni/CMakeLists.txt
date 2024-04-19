cmake_minimum_required(VERSION 3.4.1)

add_library(serial_port SHARED
        serial.c
        serial_port.c
)

find_library(log-lib log)

target_link_libraries(
        # Specifies the target library.
        serial_port
        ${log-lib}
)