package edu.dhbw.andar.pub;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import edu.dhbw.andopenglcam.R;

public class PlayMp3 {
  
	public boolean mp3Playing=true;
	public boolean mp3Pause=false;
	public boolean mp3Next=false;
    public boolean mp3Back=false;
  
    public boolean mp3Resume=true;
    public boolean checkpause=false;
    public boolean checkstatusMusic=false;
	MediaPlayer player;
	Context cont;
	public int numberSong=0;
	public int[] album={R.raw.aslongas,R.raw.myheartwillgoon,R.raw.congchuabongbong};
	PlayMp3(Context context,MediaPlayer p)
	{
		this.player=p;
		this.cont=context;
		
	}
	
	public void playMusic(  int idSong)
	{
		//if(mp3Playing==true)
		//{  
			// mp3Playing=false;
		   mp3Pause=false;
		   checkstatusMusic=true;
			player=MediaPlayer.create(cont, idSong);
			player.start();
			
			player.setOnCompletionListener(new OnCompletionListener() {
			
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					player.release();
					 //mp3Playing=true;
					numberSong++;
					if(numberSong<3)
					{
					playMusic(album[numberSong]);
					}
					else
					{   numberSong=0;
						playMusic(album[numberSong]);
					}
					
					
				}
			});
		//}
	}
	public void stopMusic()
	{   
		//
		
		
		//checkstatusMusic=false;
			
		player.stop();
		player.release();
		//mp3Stop=true;
		
		//mp3Pause=true;
		
		 
		 
	}
	public void pauseMusic()
	{     
		
            
	       
	    	 player.pause();
	    	
	    	 checkstatusMusic=true;
	     mp3Pause=true;
	    // checkstatusMusic=true;
	      
	    
	}
	public void resumeMusic()
	{           
		   
	    	 player.start();
	    	 mp3Pause=false;
	    	 checkstatusMusic=true;
		   
	    	
	    
	}

	public void backMusic()
	{   
		
			if(numberSong<=0)
			{   
				numberSong=0;
				//mp3Back=false;
				
				stopMusic();
				
				

			
				
				mp3Playing=true;
				mp3Pause=false;
				
				playMusic(album[numberSong]);
			}
			else
			{
				//mp3Back=false;
				numberSong--;
				
				stopMusic();
				
				
				mp3Playing=true;
				mp3Pause=false;
				
				playMusic(album[numberSong]);
			}
			
		 
			
	}
	public void nextMusic()
	{
		   
			if(numberSong>=2)
			{   
				numberSong=0;
				
				stopMusic();
				
				mp3Playing=true;
				mp3Pause=false;
				
				playMusic(album[numberSong]);
			}
			else
			{
				numberSong++;
			
				
				stopMusic();
				
			mp3Playing=true;
			mp3Pause=false;
			
			playMusic(album[numberSong]);
			}
		  
	}
}
