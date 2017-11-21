package com.astralbody888.alexanderconner.soundboard;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mplayer;
    AudioManager audioManager;

    public void playSound(View view, String sound){
        //String idName = view.getResources().getResourceEntryName(view.getId());

        //Log.i("inside playsound", "id Name: " + idName);
        String soundFile = sound + ".mp3";
        Log.i("inside playsound", "sound: " + soundFile);

        try {
            AssetFileDescriptor afd = getAssets().openFd(soundFile);
            mplayer = new MediaPlayer();
            mplayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mplayer.prepare();
            mplayer.start();
        }
        catch (Exception e) {
            Log.e("Error Playing file", "File not found or could not be played: /n/n" + e);
        }
    }

    public void stopSound(View view){

        mplayer.stop();
        //release mplayer or errors start
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Ice King Soundboard");
        setContentView(R.layout.activity_main);

        //Get information about the audio service the system is running on
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        SeekBar volumeControl = (SeekBar) findViewById(R.id.seekBar);
        //Set volumeMaxs
        volumeControl.setMax(maxVolume);
        volumeControl.setProgress(currentVolume);

        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.i("Setting Volume: ", Integer.toString(i));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
            }
        });

        //Get all items in assets
        String[] filelist = {""};
        AssetManager aMan = this.getAssets();
        try {
            filelist = aMan.list("");
        } catch (Exception e) {
            Log.e("File not found", "Could not get Assets in asset folder: /n/n" + e);
        }



        //Log.i("File list found: ", files);

        //Get ListView
        ListView soundListView = (ListView) findViewById(R.id.soundListView);

        final ArrayList<String> soundList = new ArrayList<String>();

       //Add file names to array, parse filenames
        for (int i = 0; i < filelist.length; i++)
        {
            //files += filelist[i] + " ";
            if (filelist[i].contains(".")) {
                String filename = filelist[i].substring(0, filelist[i].lastIndexOf('.'));
                soundList.add(filename);
            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, soundList);

        soundListView.setAdapter(arrayAdapter);

        //Set onclick methods for each list item
        soundListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("List Item tapped", soundList.get(i));
                playSound(view, soundList.get(i));
            }
        });
    }
}
