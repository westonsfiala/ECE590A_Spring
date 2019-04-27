package com.example.pinball

import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.music_fragment.*


class MusicFragment : Fragment() {

    companion object {
        fun newInstance() = MusicFragment()
    }

    // Music Player variables
    private lateinit var mMusicPlayer : MediaPlayer
    private var position = 0
    private var musicPlaying = true


    // this method is only called once for this fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // retain this fragment
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.music_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupMusic()

        musicButton.setOnClickListener {

            if(musicPlaying)
            {
                musicButton.setImageResource(android.R.drawable.ic_lock_silent_mode_off)
                pauseMusic()
            }
            else
            {
                musicButton.setImageResource(android.R.drawable.ic_lock_silent_mode)
                playMusic()
            }

            musicPlaying = !musicPlaying
        }
    }

    private fun setupMusic() {
        mMusicPlayer = MediaPlayer.create(context, R.raw.creativeminds)
        mMusicPlayer.start()
        mMusicPlayer.isLooping = true
    }

    private fun pauseMusic() {
        position = mMusicPlayer.currentPosition
        mMusicPlayer.pause()
    }

    private fun playMusic() {
        position = mMusicPlayer.currentPosition
        mMusicPlayer.seekTo(position)
        mMusicPlayer.start()
    }


    override fun onResume() {
        super.onResume()

        if(musicPlaying)
        {
            playMusic()
        }
    }

    override fun onPause() {
        super.onPause()

        if(musicPlaying)
        {
            pauseMusic()
        }
    }

}
