package app.example.spotifytest.data.user

data class UserModel(
    val display_name: String,
    val external_urls: ExternalUrls,
    val followers: Followers,
    val href: String,
    val id: String,
    val images: List<Image>,
    val type: String,
    val uri: String
)

data class ExternalUrls(
    val spotify: String
)

data class Followers(
    val href: String,
    val total: Int
)

data class Image(
    val height: Any,
    val url: String,
    val width: Any
)