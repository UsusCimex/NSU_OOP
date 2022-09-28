cmake_minimum_required(VERSION 3.5)

project(FlatMapTests CXX)

add_subdirectory(
    "${CMAKE_CURRENT_SOURCE_DIR}/googletest"
    "googletest"
)

add_executable(
    FMap
    FlatMap/FlatMap.h
)

set_target_properties(FMap PROPERTIES LINKER_LANGUAGE CXX)

add_executable(
    FTests
    FlatMap/Tests.cpp
)

# target_include_directories( #need if I want to create other dirrectory for tests
#     FTests PRIVATE
#     "${CMAKE_CURRENT_SOURCE_DIR}/FlatMap"
# )

target_link_libraries(
    FTests PRIVATE
    gtest gtest_main
)

if (MSVC)
    # https://github.com/google/googletest/blob/main/googletest/README.md  
    # Visual Studio Dynamic vs Static Runtimes

    set(gtest_force_shared_crt ON CACHE BOOL "" FORCE)

    target_compile_options(
        FMap PRIVATE
        /W4
    )

    target_compile_options(
        FTests PRIVATE
        /W4
    )
else ()
    target_compile_options(
        FMap PRIVATE
        -Wall -Wextra -pedantic -Werror
    )

    target_compile_options(
        FTests PRIVATE
        -Wall -Wextra -pedantic -Werror
    )
endif ()