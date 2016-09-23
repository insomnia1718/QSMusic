package com.zhangtao.qsmusic.control;

import com.zhangtao.qsmusic.model.Music;

/**
 * Created by tao.zhang on 16-9-9.
 */
public interface OnMusicStateListener {

    void onProgress(int position);

    void onPlay(Music music);

}
