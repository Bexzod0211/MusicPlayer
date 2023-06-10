package uz.gita.musicplayer.data.model

import java.io.Serializable

enum class CommandEnum(val amount:Int) : Serializable{
    MANAGE(0),PREV(1),NEXT(2),START(3),PAUSE(4),CLOSE(5),PLAY(6),SEEKBAR_CHANGED(7)
}