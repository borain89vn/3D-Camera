/**
	Copyright (C) 2010  Tobias Domhan

    This file is part of AndObjViewer.

    AndObjViewer is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    AndObjViewer is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with AndObjViewer.  If not, see <http://www.gnu.org/licenses/>.
 
 */
package edu.dhbw.andar.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import edu.dhbw.andar.pub.AugmentedModelViewerActivity;
import edu.dhbw.andar.util.BaseFileUtil;

public class Model implements Serializable{
	//position/rotation/scale
	public float xrot = 90;
    public float yrot = 0;
    public float zrot = 0;
    public float xpos = 0;
    public float ypos = 0;
    public float zpos = 0;
    public float scale = 5f;
    public int STATE = STATE_DYNAMIC;
    public static final int STATE_DYNAMIC = 0;
    public static final int STATE_FINALIZED = 1;
    public boolean checkFly=false;
    public int distance;
	// random speed,heigh
    public int jumpHeigh;
    public int moveDistance;
     
    // float speedRote=1/3.0f;
     float speedFall=0.05f;
     float speedJump=0.05f;
	private Vector<Group> groups = new Vector<Group>();
	/**
	 * all materials
	 */
	protected HashMap<String, Material> materials = new HashMap<String, Material>();
	
	public Model() {
		//add default material
		materials.put("default",new Material("default"));
	}
	
	public void addMaterial(Material mat) {
		//mat.finalize();
		materials.put(mat.getName(), mat);
	}
	
	public Material getMaterial(String name) {
		return materials.get(name);
	}
	
	public void addGroup(Group grp) {
		if(STATE == STATE_FINALIZED)
			grp.finalize();
		groups.add(grp);
	}
	
	public Vector<Group> getGroups() {
		return groups;
	}
	
	public void setFileUtil(BaseFileUtil fileUtil) {
		for (Iterator iterator = materials.values().iterator(); iterator.hasNext();) {
			Material mat = (Material) iterator.next();
			mat.setFileUtil(fileUtil);
		}
	}
	
	
	public HashMap<String, Material> getMaterials() {
		return materials;
	}

	public void setScale(float f) {
		this.scale += f;
		if(this.scale < 0.0001f)
			this.scale = 0.0001f;
	}

	public void setXrot(float dY) {
		this.xrot += dY;
	}

	public void setYrot(float dX) {
		this.yrot += dX;
	}

	public void setXpos(float f) {
		this.xpos += f;
	}

	public void setYpos(float f) {
		this.ypos += f;
	}
	public void setZpos(float f)
	{
		this.zpos+=f;
	}
	public void danceRotate( )
	{
		
		this.setXrot(0);
		this.setYrot(-0.08f);
		
		
	}
	public void danceFall()
	{
		this.setXrot(-1*speedFall);
		this.setYrot(0);
		if(this.xrot<45||this.xrot>135)
		{
			speedFall*=-1;
			
		}
	}
	public void danceJump( )
	{
		this.zpos=this.zpos+speedJump;
		if(this.zpos<0||this.zpos>jumpHeigh)
		{
			speedJump*=-1;
			jumpHeigh=AugmentedModelViewerActivity.randomHeigh;
			
		}
	}
	
	
	
	
	
	public void danceMove()
	{
		this.xpos=this.xpos+speedJump;
		if(this.xpos<-moveDistance||this.xpos>moveDistance)
		{
			speedJump*=-1;
			moveDistance=AugmentedModelViewerActivity.randomHeigh;
		}
	}

	
	/**
	 * convert all dynamic arrays to final non alterable ones.
	 */
	public void finalize() {
		if(STATE != STATE_FINALIZED) {
			STATE = STATE_FINALIZED;
			for (Iterator iterator = groups.iterator(); iterator.hasNext();) {
				Group grp = (Group) iterator.next();
				grp.finalize();
				grp.setMaterial(materials.get(grp.getMaterialName()));
			}
			for (Iterator<Material> iterator = materials.values().iterator(); iterator.hasNext();) {
				Material mtl = iterator.next();
				mtl.finalize();
			}
		}
	}
	
	/*
	 * get  a google protocol buffers builder, that may be serialized
	 */
	/*public BufferModel getProtocolBuffer() {
		ModelProtocolBuffer.BufferModel.Builder builder = ModelProtocolBuffer.BufferModel.newBuilder();
		
		return builder.build();
	}*/
	
}
