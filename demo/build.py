# -*- coding: utf-8 -*-
# yueran
# 2016.8.17

import sys
import os
import shutil
import traceback

g_this_file = os.path.realpath(__file__)
g_this_dir = os.path.dirname(g_this_file)

def create_jar(jar_path, manifest_path, bindir):
    cmd = "jar cmf %s %s -C %s ." % (manifest_path, jar_path, bindir)
    os.system(cmd)
 
def scan_source_file(srcdir, source_list, ext, ignore_name = []):
    names = os.listdir(srcdir)
    for name in names:
        full_path = os.path.join(srcdir, name)
        if os.path.isdir(full_path):
            scan_source_file(full_path, source_list, ext, ignore_name)
        elif name.endswith(ext):
            if not name in ignore_name:
                source_list.append(full_path)
            else:
                pass
            
def build_class(srcdir, bindir, libdir):
    source_list = list()
    scan_source_file(srcdir, source_list, ".java")
    lib_list = list()
    if os.path.isdir(libdir):
        libnames = os.listdir(libdir)
        for name in libnames:
            if name.endswith("jar"):
                full_path = os.path.join(libdir, name)
                lib_list.append(full_path)
    
    cmd = "javac -encoding UTF-8 -d %s -sourcepath %s" % (bindir, srcdir)
    java_home = os.getenv('JAVA_HOME17')
    if java_home:
        cmd = "\"%s/bin/javac\" -encoding UTF-8 -d %s -sourcepath %s" % (java_home,bindir, srcdir)
    
    if len(lib_list) !=0:
        cmd = cmd + " -cp"
    for libname in lib_list:
        cmd = cmd + " " + libname + ";"
        
    cmd = cmd[0:len(cmd)-1]
    for item in source_list:
        cmd = cmd + " " + item
     
    os.system(cmd)

def run_encrypt(class_path):
    out_path = os.path.join(os.path.dirname(class_path), os.path.basename(class_path).replace(".class", ".tmp.class"))
    try:
        tool_path = os.path.realpath("./AsmInjector.jar")
        cmd = "java -jar %s %s %s"%(tool_path, class_path, out_path)
        ret = os.system(cmd)
        if ret != 0:
            assert(0)
        if os.path.exists(class_path):
            os.remove(class_path)
        os.rename(out_path, class_path)
    except Exception as e:
        traceback.print_exc()
        if os.path.exists(out_path):
            os.remove(out_path)
    
def encrypt_string_inClass(bindir):
    enhance_list = list()
    scan_source_file(bindir, enhance_list, ".class", ["Crypto.class"])
    for item in enhance_list:
        path = os.path.realpath(item)
        run_encrypt(path)
    
def do_build():
    srcdir = os.path.join(g_this_dir, "src")
    bindir = os.path.join(g_this_dir, "bin")
    libdir = os.path.join(g_this_dir, "lib")
    
    if os.path.exists(bindir):
        shutil.rmtree(bindir)
        os.makedirs(bindir)
        
    print ("[+] build class ...")
    build_class(srcdir, bindir, libdir)

    print ("[+] enhance class ...")
    encrypt_string_inClass(bindir)
    
    print ("[+] create jar ...")
    jar_name = "demo.jar"
    manifest_name = "MANIFEST.MF"
    if os.path.exists(jar_name):
        os.remove(jar_name)
    create_jar(jar_name, manifest_name, bindir)
    print ("[+]build end.")

if __name__ == "__main__":
    java_home = os.getenv('JAVA_HOME')
    if java_home:
        print("JAVA HOME:", java_home)
    do_build()