package family.hadden.rawp

import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder

class RawpWallpaperService : WallpaperService() {
	override fun onCreateEngine(): Engine = WallpaperEngine()

	private inner class WallpaperEngine : WallpaperService.Engine() {
		override fun onSurfaceCreated(holder: SurfaceHolder?) {
			println("onSurfaceCreated")
			super.onSurfaceCreated(holder)
		}
	}
}
