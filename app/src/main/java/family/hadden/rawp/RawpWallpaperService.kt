package family.hadden.rawp

import android.app.Presentation
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.hardware.display.DisplayManager
import android.service.wallpaper.WallpaperService
import android.util.Base64
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.webkit.*
import androidx.webkit.WebViewAssetLoader
import java.io.ByteArrayOutputStream

class RawpWallpaperService : WallpaperService() {
	override fun onCreateEngine(): Engine = WallpaperEngine()

	// TODO: breakout.exe
	private inner class RawpApi(val engine: WallpaperEngine) {
		@JavascriptInterface
		fun launch(id: String) {
			val intent = packageManager.getLaunchIntentForPackage(id)
			if (intent != null) {
				startActivity(intent)
			} else {
				println("Nope: $id")
			}
		}

		@JavascriptInterface
		fun getIcon(id: String): String {
			val icon = packageManager.getApplicationIcon(id)
			val bitmap = Bitmap.createBitmap(icon.intrinsicWidth, icon.intrinsicHeight, Bitmap.Config.ARGB_8888)
			val canvas = Canvas(bitmap)
			icon.setBounds(0, 0, canvas.width, canvas.height)
			icon.draw(canvas)
			val bytes = ByteArrayOutputStream()
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
			return "data:image/png;base64," + Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT)
		}
	}

	private inner class WallpaperEngine : WallpaperService.Engine() {
		lateinit var view: WebView

		override fun onSurfaceCreated(holder: SurfaceHolder) {
			super.onSurfaceCreated(holder)

			val displayMgr = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
			val flags = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY
			val density = DisplayMetrics.DENSITY_DEFAULT

			val virtualDisplay = displayMgr.createVirtualDisplay(
				"RawpVirtualDisplay",
				desiredMinimumWidth, desiredMinimumHeight,
				density,
				holder.surface,
				flags
			)

			val pres = Presentation(displayContext, virtualDisplay.display)
			val api = RawpApi(this)
			val assetLoader = WebViewAssetLoader.Builder()
				.addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this@RawpWallpaperService))
				.build()

			view = WebView(pres.context)
			view.webViewClient = object : WebViewClient() {
				override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
					return assetLoader.shouldInterceptRequest(request.url)
				}
			}

			view.addJavascriptInterface(api, "rawp")
			view.settings.javaScriptEnabled = true
			view.loadUrl("https://appassets.androidplatform.net/assets/index.html")
//			view.loadUrl("https://google.com")

			pres.setContentView(view)
			pres.show()
		}

		override fun onTouchEvent(event: MotionEvent) {
			view.dispatchTouchEvent(event)
		}
	}
}
