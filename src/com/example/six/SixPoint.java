package com.example.six;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import android.view.View;

public class SixPoint {
	int mProgram;//��Ⱦ������ɫ�������id
	int muMVPMatrixHandle;//�ܱ任���������
	int maPositionHandle;//����λ�����Ե�����
	int maColorHandle;//������ɫ���Ե�����
	String mVertexShader;//������ɫ������ű�����.sh�ļ����������
	String mFragmentShader;//ƬԪ��ɫ���Ĵ���ű�
	static float[] mMMatrix = new float[16]; //���������3d�任���󣬰���ƽ�ƣ���ת������
	
	FloatBuffer mVertexBuffer; //�����������ݻ���
	FloatBuffer mColorBuffer; //������ɫ�����ݻ���
	int vCount;
	float xAngle = 0;//��y����ת�Ƕ�
	float yAngle = 0;//��x����ת�Ƕ�
	final float UNIT_SIZE = 1;
	
	/**
	 * 
	 * @param view ��������ͼ��
	 * @param R �����ǰ����Ľǵİ뾶
	 * @param r ������͹���Ľǵİ뾶
	 * @param z z�������
	 */
	public SixPoint(SurfaceView view, float R, float r, float z){
		initVertexData(R, r, z);
		initShader(view);
	}
	
	public void initVertexData(float R, float r, float z) {
		List<Float> fList = new ArrayList<Float>();
		float tempAngle = 360/6;
		for(float angle = 0; angle < 360; angle += 60){
			fList.add(0f);
			fList.add(0f);
			fList.add(z);
			
			fList.add((float)(R*UNIT_SIZE*Math.cos(Math.toRadians(angle))));
			fList.add((float)(R*UNIT_SIZE*Math.sin(Math.toRadians(angle))));
			fList.add(z);
			
			fList.add((float)(r*UNIT_SIZE*Math.cos(Math.toRadians(angle + tempAngle/2))));
			fList.add((float)(r*UNIT_SIZE*Math.sin(Math.toRadians(angle + tempAngle/2))));
			fList.add(z);
			
			fList.add(0f);
			fList.add(0f);
			fList.add(z);
			
			fList.add((float)(r*UNIT_SIZE*Math.cos(Math.toRadians(angle + tempAngle/2))));
			fList.add((float)(r*UNIT_SIZE*Math.sin(Math.toRadians(angle + tempAngle/2))));
			fList.add(z);
			
			fList.add((float)(R*UNIT_SIZE*Math.cos(Math.toRadians(angle + tempAngle))));
			fList.add((float)(R*UNIT_SIZE*Math.sin(Math.toRadians(angle + tempAngle))));
			fList.add(z);
		}
		
		vCount = fList.size()/3;
		float[] vertexArray = new float[fList.size()]; //��������
		for (int i = 0; i < vCount; i++) {
			vertexArray[i*3] = fList.get(i*3);
			vertexArray[i*3 + 1] = fList.get(i*3 + 1);
			vertexArray[i*3 + 2] = fList.get(i*3 + 2);
		}
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertexArray.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asFloatBuffer();
		mVertexBuffer.put(vertexArray);
		mVertexBuffer.position(0);
		
		float[] colorArray = new float[vCount*4];
		for (int i = 0; i < vCount; i++) {
			if (i % 3 == 0) {
				colorArray[i * 4] = 1;
				colorArray[i * 4 + 1] = 1;
				colorArray[i * 4 + 2] = 1;
				colorArray[i * 4 + 3] = 0;
			}else{
				colorArray[i * 4] = 0.45f;
				colorArray[i * 4 + 1] = 0.75f;
				colorArray[i * 4 + 2] = 0.75f;
				colorArray[i * 4 + 3] = 0;
			}
		}
		ByteBuffer cbb = ByteBuffer.allocateDirect(colorArray.length*4);
		cbb.order(ByteOrder.nativeOrder());
		mColorBuffer = cbb.asFloatBuffer();
		mColorBuffer.put(colorArray);
		mColorBuffer.position(0);
	}
	
	public void initShader(SurfaceView view){
		mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.sh", view.getResources());
		mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.sh", view.getResources());
		Log.i("mFragment", mFragmentShader);
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		 
		Log.i("initshader", mProgram+":"+maPositionHandle+":"+maColorHandle);
	}
	
	public void drawSelf(){
		GLES20.glUseProgram(mProgram);
		Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);
		Matrix.translateM(mMMatrix, 0, 0, 0, 1);
		Matrix.rotateM(mMMatrix, 0, yAngle, 0, 1, 0);
		Matrix.rotateM(mMMatrix, 0, xAngle, 1, 0, 0);
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1,false,MatrixState.getFinalMatrix(mMMatrix), 0);
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 3*4, mVertexBuffer);
		GLES20.glVertexAttribPointer(maColorHandle, 4, GLES20.GL_FLOAT, false, 4*4, mColorBuffer);
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maColorHandle);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
	}
	
	
	
	
	
	
	
	
	
	
	
}
