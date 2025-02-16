package com.example.mychatapp.ui.comp

import com.streamliners.pickers.media.PickedMedia

sealed class ImageState {
    data class Exists(val url : String): ImageState()
    data class New(val pickedMedia: PickedMedia):ImageState()
    data object Empty : ImageState()

    fun data(): String? {
      return when(this){
           Empty -> null
           is Exists -> url
           is New -> pickedMedia.uri
       }
    }

}