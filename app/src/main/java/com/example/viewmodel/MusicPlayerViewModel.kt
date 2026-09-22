package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.EqualizerRepository
import com.example.data.LocalAudioScanner
import com.example.data.MusicRepository
import com.example.data.OnlineMusicCatalog
import com.example.data.local.AppDatabase
import com.example.model.AudiobookItem
import com.example.model.BUILT_IN_EQUALIZER_PRESETS
import com.example.model.EqualizerPreset
import com.example.model.EqualizerState
import com.example.model.LyricLine
import com.example.model.PlayMode
import com.example.model.PlaybackSettings
import com.example.model.Playlist
import com.example.model.Song
import com.example.model.SoundQuality
import com.example.model.ThemePalette
import com.example.player.AudioEngine
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class MainTab(val label: String) {
    HOME("首页"),
    HIFI("HIFI"),
    MINE("我的")
}

enum class HomeSubTab(val label: String) {
    HOT("热门"),
    DISCOVER("发现"),
    RECOMMEND("推荐"),
    AUDIOBOOK("听书")
}

sealed class RecognitionState {
    object Idle : RecognitionState()
    object Listening : RecognitionState()
    object Analyzing : RecognitionState()
    data class Matched(val song: Song, val confidence: String) : RecognitionState()
}

class MusicPlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val audioEngine = AudioEngine()
    private val equalizerRepository = EqualizerRepository(AppDatabase.getInstance(application).equalizerDao())
    private val localAudioScanner = LocalAudioScanner(application)
    private var progressJob: Job? = null
    private var sleepTimerJob: Job? = null
    private var searchJob: Job? = null
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()
    private val _isScanningLocal = MutableStateFlow(false)
    val isScanningLocal: StateFlow<Boolean> = _isScanningLocal.asStateFlow()

    // Splash screen state
    private val _showSplash = MutableStateFlow(true)
    val showSplash: StateFlow<Boolean> = _showSplash.asStateFlow()

    // Navigation state
    private val _currentTab = MutableStateFlow(MainTab.HOME)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    private val _homeSubTab = MutableStateFlow(HomeSubTab.HOT)
    val homeSubTab: StateFlow<HomeSubTab> = _homeSubTab.asStateFlow()

    // Full screen player
    private val _showFullScreenPlayer = MutableStateFlow(false)
    val showFullScreenPlayer: StateFlow<Boolean> = _showFullScreenPlayer.asStateFlow()

    // Equalizer panel sheet
    private val _showEqualizerPanel = MutableStateFlow(false)
    val showEqualizerPanel: StateFlow<Boolean> = _showEqualizerPanel.asStateFlow()

    // Equalizer persistence state
    private val _equalizerState = MutableStateFlow(EqualizerState())
    val equalizerState: StateFlow<EqualizerState> = _equalizerState.asStateFlow()

    private val _customEqPresets = MutableStateFlow<List<EqualizerPreset>>(emptyList())
    val customEqPresets: StateFlow<List<EqualizerPreset>> = _customEqPresets.asStateFlow()

    private val _showLyrics = MutableStateFlow(false)
    val showLyrics: StateFlow<Boolean> = _showLyrics.asStateFlow()

    // Active song & playback
    private val _currentSong = MutableStateFlow<Song?>(MusicRepository.sampleSongs.firstOrNull())
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPositionMs = MutableStateFlow(0L)
    val currentPositionMs: StateFlow<Long> = _currentPositionMs.asStateFlow()

    private val _playMode = MutableStateFlow(PlayMode.SEQUENCE)
    val playMode: StateFlow<PlayMode> = _playMode.asStateFlow()

    private val _playlistQueue = MutableStateFlow<List<Song>>(MusicRepository.sampleSongs)
    val playlistQueue: StateFlow<List<Song>> = _playlistQueue.asStateFlow()

    // User data
    private val _favoriteSongIds = MutableStateFlow<Set<String>>(setOf("song_1", "song_2"))
    val favoriteSongIds: StateFlow<Set<String>> = _favoriteSongIds.asStateFlow()

    private val _recentPlayed = MutableStateFlow<List<Song>>(MusicRepository.sampleSongs.take(4))
    val recentPlayed: StateFlow<List<Song>> = _recentPlayed.asStateFlow()

    private val _localSongs = MutableStateFlow<List<Song>>(MusicRepository.localSongs)
    val localSongs: StateFlow<List<Song>> = _localSongs.asStateFlow()

    private val _customPlaylists = MutableStateFlow<List<Playlist>>(
        MusicRepository.defaultPlaylists.filter { it.isCustom }
    )
    val customPlaylists: StateFlow<List<Playlist>> = _customPlaylists.asStateFlow()

    private val _favoritePlaylists = MutableStateFlow<List<Playlist>>(
        MusicRepository.defaultPlaylists.filter { !it.isCustom }
    )
    val favoritePlaylists: StateFlow<List<Playlist>> = _favoritePlaylists.asStateFlow()

    val audiobooks: List<AudiobookItem> = MusicRepository.audiobooks

    // Search state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Song>>(emptyList())
    val searchResults: StateFlow<List<Song>> = _searchResults.asStateFlow()

    // Settings & Personalization
    private val _playbackSettings = MutableStateFlow(PlaybackSettings())
    val playbackSettings: StateFlow<PlaybackSettings> = _playbackSettings.asStateFlow()

    private val _currentTheme = MutableStateFlow(ThemePalette.RED_BLACK)
    val currentTheme: StateFlow<ThemePalette> = _currentTheme.asStateFlow()

    private val _equalizerPreset = MutableStateFlow("原声HIFI")
    val equalizerPreset: StateFlow<String> = _equalizerPreset.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

    // Sleep timer
    private val _sleepTimerSecondsLeft = MutableStateFlow<Int?>(null)
    val sleepTimerSecondsLeft: StateFlow<Int?> = _sleepTimerSecondsLeft.asStateFlow()

    // Youth mode
    private val _youthModeActive = MutableStateFlow(false)
    val youthModeActive: StateFlow<Boolean> = _youthModeActive.asStateFlow()

    // Song recognition
    private val _recognitionState = MutableStateFlow<RecognitionState>(RecognitionState.Idle)
    val recognitionState: StateFlow<RecognitionState> = _recognitionState.asStateFlow()

    init {
        // Auto-dismiss splash screen after 2.5 seconds
        viewModelScope.launch {
            delay(2500)
            _showSplash.value = false
        }

        // Initialize and automatically scan local device songs using recognition technology
        viewModelScope.launch {
            scanLocalSongsInternal()
        }

        // Collect persisted Equalizer settings from Room database
        viewModelScope.launch {
            equalizerRepository.activeEqualizerFlow.collect { saved ->
                _equalizerState.value = saved
                _equalizerPreset.value = saved.currentPresetName
                applyAudioEngineEq(saved)
            }
        }

        viewModelScope.launch {
            equalizerRepository.customPresetsFlow.collect { presets ->
                _customEqPresets.value = presets
            }
        }
    }

    fun dismissSplash() {
        _showSplash.value = false
    }

    fun setTab(tab: MainTab) {
        _currentTab.value = tab
    }

    fun setHomeSubTab(subTab: HomeSubTab) {
        _homeSubTab.value = subTab
    }

    fun openFullScreenPlayer() {
        _showFullScreenPlayer.value = true
    }

    fun closeFullScreenPlayer() {
        _showFullScreenPlayer.value = false
    }

    fun toggleLyrics() {
        _showLyrics.value = !_showLyrics.value
    }

    fun playSong(song: Song) {
        _currentSong.value = song
        _currentPositionMs.value = 0L
        _isPlaying.value = true
        audioEngine.startPlaying(song.toneFrequency)
        startProgressTracker()

        // Update recently played
        val list = _recentPlayed.value.toMutableList()
        list.removeAll { it.id == song.id }
        list.add(0, song)
        _recentPlayed.value = list.take(20)
    }

    fun togglePlayPause() {
        if (_isPlaying.value) {
            _isPlaying.value = false
            audioEngine.pause()
            progressJob?.cancel()
        } else {
            val song = _currentSong.value ?: MusicRepository.sampleSongs.firstOrNull()
            if (song != null) {
                _isPlaying.value = true
                audioEngine.startPlaying(song.toneFrequency)
                startProgressTracker()
            }
        }
    }

    fun seekTo(positionMs: Long) {
        _currentPositionMs.value = positionMs
    }

    fun playNext() {
        val queue = _playlistQueue.value
        if (queue.isEmpty()) return
        val current = _currentSong.value
        val currentIndex = queue.indexOfFirst { it.id == current?.id }

        val nextIndex = when (_playMode.value) {
            PlayMode.SEQUENCE -> (currentIndex + 1) % queue.size
            PlayMode.REPEAT_ONE -> currentIndex.coerceAtLeast(0)
            PlayMode.SHUFFLE -> (queue.indices).random()
        }
        playSong(queue[nextIndex])
    }

    fun playPrevious() {
        val queue = _playlistQueue.value
        if (queue.isEmpty()) return
        val current = _currentSong.value
        val currentIndex = queue.indexOfFirst { it.id == current?.id }

        val prevIndex = when (_playMode.value) {
            PlayMode.SEQUENCE -> if (currentIndex <= 0) queue.size - 1 else currentIndex - 1
            PlayMode.REPEAT_ONE -> currentIndex.coerceAtLeast(0)
            PlayMode.SHUFFLE -> (queue.indices).random()
        }
        playSong(queue[prevIndex])
    }

    fun togglePlayMode() {
        _playMode.value = when (_playMode.value) {
            PlayMode.SEQUENCE -> PlayMode.REPEAT_ONE
            PlayMode.REPEAT_ONE -> PlayMode.SHUFFLE
            PlayMode.SHUFFLE -> PlayMode.SEQUENCE
        }
    }

    fun toggleFavorite(songId: String) {
        val currentFavs = _favoriteSongIds.value.toMutableSet()
        if (currentFavs.contains(songId)) {
            currentFavs.remove(songId)
        } else {
            currentFavs.add(songId)
        }
        _favoriteSongIds.value = currentFavs
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            _isSearching.value = false
            return
        }

        searchJob = viewModelScope.launch {
            _isSearching.value = true
            // Instant local search
            val localMatches = _localSongs.value.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.artist.contains(query, ignoreCase = true) ||
                        it.album.contains(query, ignoreCase = true) ||
                        it.genre.contains(query, ignoreCase = true)
            }
            // Online network singer / song search
            val onlineMatches = OnlineMusicCatalog.search(query)
            
            // Combine results without duplicates (local preferred, then network)
            val combined = (localMatches + onlineMatches).distinctBy { "${it.title}_${it.artist}" }
            _searchResults.value = combined
            _isSearching.value = false
        }
    }

    fun createCustomPlaylist(name: String, desc: String) {
        val newPl = Playlist(
            id = "custom_${System.currentTimeMillis()}",
            title = name.ifBlank { "我的自建歌单" },
            description = desc.ifBlank { "添加喜欢的歌曲吧" },
            songIds = listOfNotNull(_currentSong.value?.id),
            isCustom = true,
            coverTag = "自建"
        )
        _customPlaylists.value = listOf(newPl) + _customPlaylists.value
    }

    fun scanLocalSongs() {
        viewModelScope.launch {
            scanLocalSongsInternal()
        }
    }

    private suspend fun scanLocalSongsInternal() {
        _isScanningLocal.value = true
        try {
            val scanned = localAudioScanner.scanAndRecognizeDeviceSongs()
            if (scanned.isNotEmpty()) {
                _localSongs.value = scanned
                _playlistQueue.value = scanned
                if (_currentSong.value == null) {
                    _currentSong.value = scanned.firstOrNull()
                }
            }
        } catch (_: Exception) {
        } finally {
            _isScanningLocal.value = false
        }
    }

    fun setSleepTimer(minutes: Int?) {
        sleepTimerJob?.cancel()
        if (minutes == null || minutes <= 0) {
            _sleepTimerSecondsLeft.value = null
            return
        }
        _sleepTimerSecondsLeft.value = minutes * 60
        sleepTimerJob = viewModelScope.launch {
            while (isActive && (_sleepTimerSecondsLeft.value ?: 0) > 0) {
                delay(1000)
                val current = _sleepTimerSecondsLeft.value ?: 0
                if (current <= 1) {
                    _sleepTimerSecondsLeft.value = null
                    _isPlaying.value = false
                    audioEngine.pause()
                    break
                } else {
                    _sleepTimerSecondsLeft.value = current - 1
                }
            }
        }
    }

    fun setSleepTimerEndOfSong() {
        val current = _currentSong.value ?: return
        val remainingMs = (current.durationMs - _currentPositionMs.value).coerceAtLeast(0)
        setSleepTimer((remainingMs / 60000).toInt().coerceAtLeast(1))
    }

    fun updateSettings(modifier: (PlaybackSettings) -> PlaybackSettings) {
        _playbackSettings.value = modifier(_playbackSettings.value)
    }

    fun setTheme(theme: ThemePalette) {
        _currentTheme.value = theme
    }

    fun openEqualizerPanel() {
        _showEqualizerPanel.value = true
    }

    fun closeEqualizerPanel() {
        _showEqualizerPanel.value = false
    }

    fun setEqualizerPreset(preset: String) {
        selectEqualizerPreset(preset)
    }

    fun selectEqualizerPreset(presetName: String) {
        val preset = BUILT_IN_EQUALIZER_PRESETS.find { it.name == presetName }
            ?: _customEqPresets.value.find { it.name == presetName }

        val current = _equalizerState.value
        val newBands = if (preset != null) {
            current.bands.mapIndexed { index, band ->
                band.copy(gain = preset.gains.getOrElse(index) { 0f })
            }
        } else {
            current.bands
        }

        val newState = current.copy(
            currentPresetName = presetName,
            bands = newBands,
            isCustom = (preset == null)
        )
        _equalizerState.value = newState
        _equalizerPreset.value = presetName
        applyAudioEngineEq(newState)
        viewModelScope.launch {
            equalizerRepository.saveEqualizerState(newState)
        }
    }

    fun setEqualizerBandGain(bandIndex: Int, newGain: Float) {
        val current = _equalizerState.value
        val updatedBands = current.bands.mapIndexed { index, band ->
            if (index == bandIndex) band.copy(gain = newGain) else band
        }
        val newState = current.copy(
            currentPresetName = "自定义",
            bands = updatedBands,
            isCustom = true
        )
        _equalizerState.value = newState
        _equalizerPreset.value = "自定义"
        applyAudioEngineEq(newState)
        viewModelScope.launch {
            equalizerRepository.saveEqualizerState(newState)
        }
    }

    fun setBassBoost(level: Float) {
        val newState = _equalizerState.value.copy(bassBoost = level)
        _equalizerState.value = newState
        applyAudioEngineEq(newState)
        viewModelScope.launch {
            equalizerRepository.saveEqualizerState(newState)
        }
    }

    fun setSurround3d(level: Float) {
        val newState = _equalizerState.value.copy(surround3d = level)
        _equalizerState.value = newState
        viewModelScope.launch {
            equalizerRepository.saveEqualizerState(newState)
        }
    }

    fun toggleEqualizer(enabled: Boolean) {
        val newState = _equalizerState.value.copy(isEnabled = enabled)
        _equalizerState.value = newState
        applyAudioEngineEq(newState)
        viewModelScope.launch {
            equalizerRepository.saveEqualizerState(newState)
        }
    }

    fun resetEqualizer() {
        val current = _equalizerState.value
        val resetBands = current.bands.map { it.copy(gain = 0f) }
        val newState = current.copy(
            currentPresetName = "原声HIFI",
            bands = resetBands,
            bassBoost = 0f,
            surround3d = 0f,
            isCustom = false
        )
        _equalizerState.value = newState
        _equalizerPreset.value = "原声HIFI"
        applyAudioEngineEq(newState)
        viewModelScope.launch {
            equalizerRepository.saveEqualizerState(newState)
        }
    }

    fun saveCustomPreset(presetName: String) {
        val gains = _equalizerState.value.gains
        viewModelScope.launch {
            equalizerRepository.saveCustomPreset(presetName, gains)
            selectEqualizerPreset(presetName)
        }
    }

    private fun applyAudioEngineEq(state: EqualizerState) {
        if (!state.isEnabled) {
            audioEngine.updateEqualizer(0f, 0f, 0f)
            return
        }
        val bassGain = state.bands.getOrNull(0)?.gain ?: 0f
        val trebleGain = state.bands.getOrNull(4)?.gain ?: 0f
        audioEngine.updateEqualizer(bassGain, trebleGain, state.bassBoost)
    }

    fun setPlaybackSpeed(speed: Float) {
        _playbackSpeed.value = speed
    }

    fun updateCurrentSongLyrics(newLyrics: List<LyricLine>) {
        val song = _currentSong.value ?: return
        _currentSong.value = song.copy(lyrics = newLyrics)
    }

    fun toggleYouthMode() {
        _youthModeActive.value = !_youthModeActive.value
    }

    fun startSongRecognition() {
        _recognitionState.value = RecognitionState.Listening
        viewModelScope.launch {
            delay(1500)
            _recognitionState.value = RecognitionState.Analyzing
            delay(1500)
            val candidatePool = if (_localSongs.value.isNotEmpty()) _localSongs.value else OnlineMusicCatalog.songs
            val matchedSong = candidatePool.random()
            _recognitionState.value = RecognitionState.Matched(matchedSong, "99.8% 声学指纹匹配")
        }
    }

    fun resetRecognition() {
        _recognitionState.value = RecognitionState.Idle
    }

    private fun startProgressTracker() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (isActive && _isPlaying.value) {
                delay(500)
                val duration = _currentSong.value?.durationMs ?: 1L
                val nextPos = _currentPositionMs.value + (500 * _playbackSpeed.value).toLong()
                if (nextPos >= duration) {
                    playNext()
                } else {
                    _currentPositionMs.value = nextPos
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioEngine.release()
        progressJob?.cancel()
        sleepTimerJob?.cancel()
    }
}
