package family.hadden.rawp

import android.app.Activity
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.View

class SetWallpaperActivity : Activity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
		intent.putExtra(
			WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
			ComponentName(this, RawpWallpaperService::class.java)
		)
		startActivity(intent)
	}
}
