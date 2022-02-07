package family.hadden.rawp

import android.graphics.Color
import android.graphics.Paint
import android.service.wallpaper.WallpaperService
import android.view.MotionEvent
import android.view.SurfaceHolder
import kotlin.random.Random

class RawpWallpaperService : WallpaperService() {
	override fun onCreateEngine(): Engine = WallpaperEngine()

	private inner class WallpaperEngine : WallpaperService.Engine() {
		override fun onSurfaceCreated(holder: SurfaceHolder) {
			drawRandomBG(holder)
		}

		override fun onTouchEvent(event: MotionEvent?) {
			if (event?.action == MotionEvent.ACTION_DOWN) {
				drawRandomBG(surfaceHolder)
			}
		}

		private fun drawRandomBG(holder: SurfaceHolder) {
			val canvas = holder.lockCanvas()

			val paint = Paint().apply {
				val randomColor = Random.nextInt(16_777_216)
					.toString(16)
					.padStart(6, '0')
				color = Color.parseColor("#$randomColor")
				style = Paint.Style.FILL
			}
			canvas.drawPaint(paint)

			holder.unlockCanvasAndPost(canvas)
		}
	}
}
