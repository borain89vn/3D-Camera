package edu.dhbw.andar.models;

import edu.dhbw.andar.ARToolkit;
import edu.dhbw.andar.graphics.Model3D;

public class ManageARObject{
	
	public int idMaker;
	//public double [] pos;
	private ARToolkit artoolkit;
	
	public ManageARObject(ARToolkit artoolkit,Model3D model3d)
	{
		this.artoolkit=artoolkit;
		
	}
	public Model3D changeModel(String fileName,Model model,Model3D model3d)
	{   
		
		
        
		return null;
		
	}
	public void DeleteModel(Model3D model3d)
	{
		artoolkit.unregisterARObject(model3d);
	}
	
	

}
