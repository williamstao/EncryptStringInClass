package com.yueran.asminject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
//import org.objectweb.asm.commons.LocalVariablesSorter;
public class BytecodeInject {
    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException
    {
    	if(args.length < 2)
    	{
    		System.out.println("paramters error");
    		System.exit(0);
    	}
    	String in = args[0];
    	String out = args[1];
        FileInputStream fis = new FileInputStream(in);
        ClassReader cr = new ClassReader(fis);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cr.accept(new MyClassVisivator(cw), 0);
        writeToFile(cw.toByteArray(), out);
    }
    
    static void writeToFile(byte[] bytes, String fileName) 
    {
        try 
        {
            (new File(fileName)).createNewFile();
        } 
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        FileOutputStream fos = null;
        try 
        {
            fos = new FileOutputStream(fileName);
            fos.write(bytes);
            fos.flush();
        } 
        catch (Exception e) 
        {
        }
        finally
        {
            try 
            {
                if (fos != null) 
                {
                    fos.close();
                }
            }
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
    }
  
    static class MyClassVisivator extends ClassAdapter 
    {
        ClassVisitor mCv;
        public MyClassVisivator(ClassVisitor cv) 
        {
            super(cv);
            mCv = cv;
        }
        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
        {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
            MyMethodVisitor mmv = new MyMethodVisitor(access, desc, mv);
            return mmv;
        }
    }
    
    static class MyMethodVisitor extends MethodAdapter 
    {
        MethodVisitor mMv;
        public MyMethodVisitor(int access, String desc,MethodVisitor mv) 
        {
            super(mv);
            mMv = mv;
        }
        @Override 
        public void visitCode() 
        {
            super.visitCode();                 
        }
        
        @Override 
        public void visitLdcInsn(Object cst){
        	if(cst instanceof String)
        	{       	
        		//System.out.println(cst);
        		String encData = Encryt.encryptString(cst.toString());
        		try 
        		{
					mv.visitLdcInsn(encData);
				} 
        		catch (Exception e) 
        		{
					e.printStackTrace();
				} 
        		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "Crypto", "DecryptString", "(Ljava/lang/String;)Ljava/lang/String;");
        		return;
        	}
        	super.visitLdcInsn(cst);
        }
        
        @Override 
        public void visitEnd() {
            super.visitEnd();
        }
    }
}