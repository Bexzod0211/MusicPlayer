package uz.gita.musicplayer.di

import dagger.Component
import uz.gita.musicplayer.service.MusicService

@Component(modules = [RepositoryModule::class])
interface MyServiceComponent {
    fun inject(service: MusicService)

}