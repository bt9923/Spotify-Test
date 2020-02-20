package app.example.spotifytest.data

data class UserTracksFromPlaylist(
    val collaborative: Boolean,
    val description: String,
    val external_urls: ExternalUrls?,
    val followers: Followers?,
    val href: String,
    val id: String,
    val images: List<Image>?,
    val name: String,
    val owner: Owner?,
    val primary_color: Any?,
    val public: Boolean,
    val snapshot_id: String,
    val tracks: TracksItem?,
    val type: String,
    val uri: String
){
    constructor() : this(false, ""
    , null, null, "", "", null, ""
    , null, null, false, "", null, "", "")
}

//data class ExternalUrls(
//    val spotify: String
//)
//
//data class Followers(
//    val href: Any,
//    val total: Int
//)
//
//data class Image(
//    val height: Int,
//    val url: String,
//    val width: Int
//)
//
//data class Owner(
//    val display_name: String,
//    val external_urls: ExternalUrlsX,
//    val href: String,
//    val id: String,
//    val type: String,
//    val uri: String
//)

data class ExternalUrlsX(
    val spotify: String
)

data class TracksItem(
    val href: String,
    val items: List<ItemTrack>,
    val limit: Int,
    val next: Any,
    val offset: Int,
    val previous: Any,
    val total: Int
)

data class ItemTrack(
    val added_at: String,
    val added_by: AddedBy,
    val is_local: Boolean,
    val primary_color: Any,
    val track: Track,
    val video_thumbnail: VideoThumbnail
)

data class AddedBy(
    val external_urls: ExternalUrlsXX,
    val href: String,
    val id: String,
    val type: String,
    val uri: String
)

data class ExternalUrlsXX(
    val spotify: String
)

data class Track(
    val album: Album,
    val artists: List<ArtistX>,
    val available_markets: List<String>,
    val disc_number: Int,
    val duration_ms: Int,
    val episode: Boolean,
    val explicit: Boolean,
    val external_ids: ExternalIds,
    val external_urls: ExternalUrlsXXXXXX,
    val href: String,
    val id: String,
    val is_local: Boolean,
    val name: String,
    val popularity: Int,
    val preview_url: String,
    val track: Boolean,
    val track_number: Int,
    val type: String,
    val uri: String
)

data class Album(
    val album_type: String,
    val artists: List<Artist>,
    val available_markets: List<String>,
    val external_urls: ExternalUrlsXXXX,
    val href: String,
    val id: String,
    val images: List<ImageX>,
    val name: String,
    val release_date: String,
    val release_date_precision: String,
    val total_tracks: Int,
    val type: String,
    val uri: String
)

data class Artist(
    val external_urls: ExternalUrlsXXX,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)

data class ExternalUrlsXXX(
    val spotify: String
)

data class ExternalUrlsXXXX(
    val spotify: String
)

data class ImageX(
    val height: Int,
    val url: String,
    val width: Int
)

data class ArtistX(
    val external_urls: ExternalUrlsXXXXX,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)

data class ExternalUrlsXXXXX(
    val spotify: String
)

data class ExternalIds(
    val isrc: String
)

data class ExternalUrlsXXXXXX(
    val spotify: String
)

data class VideoThumbnail(
    val url: Any
)