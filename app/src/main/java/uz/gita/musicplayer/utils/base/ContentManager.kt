package uz.gita.musicplayer.utils.base

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.gita.musicplayer.MainActivity
import uz.gita.musicplayer.data.model.MusicData

private val projection = arrayOf(
    MediaStore.Audio.Media._ID,
    MediaStore.Audio.Media.TITLE,
    MediaStore.Audio.Media.ARTIST,
    MediaStore.Audio.Media.ALBUM_ID,
    MediaStore.Audio.Media.DATA,
    MediaStore.Audio.Media.DURATION
)
private val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

private val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

fun Context.getAllMusics(): List<MusicData> {
    val result = mutableListOf<MusicData>()



    val cursor = contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        null,
        sortOrder
    )

    cursor?.let {
        if (cursor.moveToFirst()) {
            do {
                val musicId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID)?:0)
                val musicName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)?:0)
                val musicArtist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)?:0)
                val albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)?:0)
                val data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)?:0)
                val duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)?:0)
                result.add(MusicData(musicId,musicName,musicArtist,albumId,data,duration))
            }
            while (cursor.moveToNext())
        }
    }

    cursor?.close()

    return result
}


fun Context.getMusicCursor():Flow<Cursor> = flow {
    val cursor:Cursor =contentResolver.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        null,
        sortOrder
    )?:return@flow

    emit(cursor)
}

fun Cursor.getMusicByPos(pos:Int):MusicData {
    this.moveToPosition(pos)
    val musicId = getLong(getColumnIndex(MediaStore.Audio.Media._ID)?:0)
    val musicName = getString(getColumnIndex(MediaStore.Audio.Media.TITLE)?:0)
    val musicArtist = getString(getColumnIndex(MediaStore.Audio.Media.ARTIST)?:0)
    val albumId = getLong(getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)?:0)
    val data = getString(getColumnIndex(MediaStore.Audio.Media.DATA)?:0)
    val duration = getLong(getColumnIndex(MediaStore.Audio.Media.DURATION)?:0)
    return MusicData(musicId,musicName,musicArtist,albumId, data, duration)
}
