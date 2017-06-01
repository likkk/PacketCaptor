package com.tqd.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.Environment;
import android.util.Log;

public class FileUtils {
	
	
	private static final String tag = "FileUtils";


	public void moveFileToSDCard(){
		
		 
		
		
	}
	
	public static File copyFile(InputStream in,String dstDir,String dstFileName){
		
		
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		
		int temp=-1;
		byte[] buffer=new byte[1024];
		try {
			while((temp=in.read(buffer))!=-1){
				
				baos.write(buffer, 0, temp);
			}
			
			baos.flush();
			in.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(baos.size()>0){
			
			return writeFile(dstDir, dstFileName, baos.toByteArray());
		}
		
		return null;
		
	}
	
	public static File writeFile(String dir,String fileName,byte []data){
		
		 File root=getSDCardPath();
		 if(root==null){
			 
			 Log.i(tag, "the root file is null");
			 return null;
			 
		 }
		 String rootPath=root.getAbsolutePath()+File.separator+dir;
		 
		 if(makeDirectory(rootPath)){
			 
			 File file=new File(rootPath+File.separator+fileName);
			 if(!file.exists()){
				 Log.i(tag, "create a new file "+file.getAbsolutePath());
				 try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 FileOutputStream fos=null;
			try {
				
				fos = new FileOutputStream(file);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 if(fos==null){
				 
				 return null;
			 }
			 try {
				 
				 fos.write(data);
				 fos.flush();
				 fos.close();
				 return file;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			 
		 }
		
		return null;
		
	}
	
	public static boolean makeDirectory(String dir){
		
		
		File file=new File(dir);
		if(file.exists() || file.mkdir()){
			
			return true;
		}
		
		return false;
		
	}
	
	public static File getSDCardPath(){
		
		
		String state=Environment.getExternalStorageState();
		
		if(state.equals(Environment.MEDIA_MOUNTED)){

			File file=Environment.getExternalStorageDirectory();
			
			return file;
			
		}else{
			
			Log.i(tag, "the phone dones not has a sdcard");
		}
		 
		return null;
		
	}
	

}
