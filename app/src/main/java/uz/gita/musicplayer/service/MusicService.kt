package uz.gita.musicplayer.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.gita.musicplayer.MainActivity
import uz.gita.musicplayer.R
import uz.gita.musicplayer.data.model.CommandEnum
import uz.gita.musicplayer.data.model.MusicData
import uz.gita.musicplayer.data.source.local.AppDatabase
import uz.gita.musicplayer.utils.MyEventBus
import uz.gita.musicplayer.utils.base.getMusicByPos
import uz.gita.musicplayer.utils.myLog
import kotlin.random.Random

class MusicService : Service() {
    private val CHANNEL_ID = "Bekhzod's Music player"

    private val dao = AppDatabase.getInstance().getMusicDao()
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    var _musicPlayer: MediaPlayer? = null
    val musicPlayer get() = _musicPlayer!!
    var job: Job? = null
    val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
    override fun onCreate() {
        super.onCreate()
        createChannel()
//        myLog("onCreate MusicService")
        // Initialize Dagger and inject dependencies

    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_MIN
            val notificationChannel = NotificationChannel(CHANNEL_ID, "Music player", importance)

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


    private fun createNotification(music: MusicData) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_music)
            .setCustomContentView(createRemoteView(music))
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setOngoing(true)
            .setContentIntent(pendingIntent)


        startForeground(1, notificationBuilder.build())
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (MyEventBus.cursor == null) return START_NOT_STICKY
        val command = intent?.extras?.getSerializable("COMMAND") as CommandEnum
        val music = if (MyEventBus.selectedPos !=- 1)  MyEventBus.cursor!!.getMusicByPos(MyEventBus.selectedPos)
        else dao.getAllFavouritesMusics()[MyEventBus.selectedFavPos]
        createNotification(music)

        doneCommand(command)
        return START_NOT_STICKY
    }

    private fun createRemoteView(music: MusicData): RemoteViews {
        val view = RemoteViews(packageName, R.layout.remote_view)

        val albumId = music.albumId
        val uri = Uri.parse("content://media/external/audio/albumart/$albumId")
        if (music.albumId == 6539316500227728566 || music.albumId == 2138260763037359727) {
            view.setImageViewResource(R.id.image, R.drawable.ic_music)
        } else {
            view.setImageViewUri(R.id.image, uri)
        }

        if (_musicPlayer != null && musicPlayer.isPlaying) {
            myLog("music is playing ${musicPlayer.isPlaying}")
            view.setImageViewResource(R.id.buttonManage, R.drawable.ic_play_black)
        } else {
            view.setImageViewResource(R.id.buttonManage, R.drawable.ic_pause_black)
        }
        view.setTextViewText(R.id.textMusicName, music.name)
        view.setTextViewText(R.id.textArtistName, music.artist)

        view.setOnClickPendingIntent(R.id.buttonManage, createPendingIntent(CommandEnum.MANAGE))
        view.setOnClickPendingIntent(R.id.buttonPrev, createPendingIntent(CommandEnum.PREV))
        view.setOnClickPendingIntent(R.id.buttonNext, createPendingIntent(CommandEnum.NEXT))
        view.setOnClickPendingIntent(R.id.buttonCancel, createPendingIntent(CommandEnum.CLOSE))


        return view
    }

    private fun createPendingIntent(command: CommandEnum): PendingIntent {
        val intent = Intent(this, MusicService::class.java)
        intent.putExtra("COMMAND", command)
        return PendingIntent.getService(this, command.amount, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
    }


    private fun doneCommand(command: CommandEnum) {
        when (command) {
            CommandEnum.MANAGE -> {

                if (musicPlayer.isPlaying) {
                    doneCommand(CommandEnum.PAUSE)
                } else {
                    doneCommand(CommandEnum.PLAY)
                }
            }

            CommandEnum.PREV -> {
                var currentTime = 0
                MyEventBus.currentTimeFlow.onEach {
                    currentTime = it
                }.launchIn(scope)
//                myLog("${MyEventBus.currentTime}")
                if (currentTime > 10_000) {
                    MyEventBus.currentTime = 0
                    musicPlayer.seekTo(0)
                    MyEventBus.musicIsPlaying.onEach {
                        if (it)
                            startNewJob()
                    }.launchIn(scope)
                } else {
                    if (MyEventBus.selectedPos != -1) {
                        var nextPos = 0
                        if (MyEventBus.isShuffle) {
                            nextPos = Random.nextInt(0, MyEventBus.cursor!!.count)
                        } else {
                            nextPos = if (MyEventBus.selectedPos == 0) {
                                MyEventBus.cursor!!.count - 1
                            } else {
                                --MyEventBus.selectedPos
                            }
                        }
                        MyEventBus.selectedPos = nextPos
                    } else if (MyEventBus.selectedFavPos != -1){
                        var nextPos = 0
                        if (MyEventBus.isShuffle) {
                            nextPos = Random.nextInt(0, dao.getAllFavouritesMusics().size)
                        } else {
                            nextPos = if (MyEventBus.selectedFavPos == 0) {
                                dao.getAllFavouritesMusics().size - 1
                            } else {
                                --MyEventBus.selectedFavPos
                            }
                        }
                        MyEventBus.selectedFavPos = nextPos
                    }
                    doneCommand(CommandEnum.START)
                }
            }

            CommandEnum.PLAY -> {
                musicPlayer.seekTo(MyEventBus.currentTime)
                musicPlayer.start()
                startNewJob()
                scope.launch {
                    MyEventBus.musicIsPlaying.emit(musicPlayer.isPlaying)
                }
            }

            CommandEnum.START -> {
                _musicPlayer?.stop()
                MyEventBus.currentTime = 0
                var data:MusicData? = null
                if (MyEventBus.selectedPos !=- 1) {
                     data = MyEventBus.cursor!!.getMusicByPos(MyEventBus.selectedPos)

                    _musicPlayer = MediaPlayer.create(this, Uri.parse(data.data))

                    scope.launch {
//                        myLog("MyEventBus.selectedMusicFlow.emit(data) in MusicService")
                        MyEventBus.selectedMusicFlow.emit(data)
                    }



//                MyEventBus.currentTimeFlow.onEach {
//                    musicPlayer.seekTo(it)
//                }.launchIn(scope)



                }else if (MyEventBus.selectedFavPos !=- 1){
                    data = dao.getAllFavouritesMusics()[MyEventBus.selectedFavPos]
                    _musicPlayer = MediaPlayer.create(this, Uri.parse(data.data))
                    scope.launch {
                        myLog("MyEventBus.selectedFavMusicFlow.emit(data) in MusicService")
                        MyEventBus.selectedFavMusicFlow.emit(data)
                    }
//                MyEventBus.currentTimeFlow.onEach {
//                    musicPlayer.seekTo(it)
//                }.launchIn(scope)
                }
                MyEventBus.totalTime = data!!.duration
                startNewJob()
                musicPlayer.setOnCompletionListener {
                    if (MyEventBus.isRepeatOne) {
                        doneCommand(CommandEnum.START)
                    } else {
                        doneCommand(CommandEnum.NEXT)
                    }
                }
                musicPlayer.start()
                scope.launch {
                    MyEventBus.musicIsPlaying.emit(musicPlayer.isPlaying)
                    MyEventBus.changeFlow.emit(true)
                }
            }

            CommandEnum.PAUSE -> {
                musicPlayer.pause()
                MyEventBus.currentTimeFlow.onEach {
                    MyEventBus.currentTime = it
                }.launchIn(scope)
                job?.cancel()
                scope.launch {
                    MyEventBus.musicIsPlaying.emit(musicPlayer.isPlaying)
                }
            }

            CommandEnum.NEXT -> {
                if (MyEventBus.selectedPos != -1) {
                    var nextPos = 0
                    if (MyEventBus.isShuffle) {
                        nextPos = Random.nextInt(0, MyEventBus.cursor!!.count)
                    } else {
                        nextPos = if (MyEventBus.selectedPos == MyEventBus.cursor!!.count - 1) {
                            0
                        } else {
                            ++MyEventBus.selectedPos
                        }
                    }
                    MyEventBus.selectedPos = nextPos
                }else if (MyEventBus.selectedFavPos != -1){
                    var nextPos = 0
                    if (MyEventBus.isShuffle) {
                        nextPos = Random.nextInt(0, dao.getAllFavouritesMusics().size)
                    } else {
                        nextPos = if (MyEventBus.selectedFavPos == dao.getAllFavouritesMusics().size - 1) {
                            0
                        } else {
                            ++MyEventBus.selectedFavPos
                        }
                    }
                    MyEventBus.selectedFavPos = nextPos
                }
                doneCommand(CommandEnum.START)
            }

            CommandEnum.CLOSE -> {
                musicPlayer.stop()
                scope.launch {
                    MyEventBus.musicIsPlaying.emit(musicPlayer.isPlaying)
                }
                ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
            }

            CommandEnum.SEEKBAR_CHANGED -> {
                startNewJob()
            }

            CommandEnum.SEEK_TO -> {
                MyEventBus.musicIsPlaying.onEach {
                    if (!it){
                        job?.cancel()
                    }
                }.launchIn(scope)
                musicPlayer.seekTo(MyEventBus.currentTime)
            }
        }
    }

    private fun moveProgress(): Flow<Int> = flow {
        for (i in MyEventBus.currentTime until MyEventBus.totalTime step 1000) {
            emit(i.toInt())
            delay(1000)
        }
    }.flowOn(Dispatchers.IO)

    private fun startNewJob() {
        job?.cancel()
        job = moveProgress().onEach {
            MyEventBus.currentTimeFlow.emit(it)
        }.launchIn(scope)
    }

}