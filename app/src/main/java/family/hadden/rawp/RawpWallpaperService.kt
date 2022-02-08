package family.hadden.rawp

import android.app.Presentation
import android.content.Context
import android.hardware.display.DisplayManager
import android.service.wallpaper.WallpaperService
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.webkit.WebViewAssetLoader

class RawpWallpaperService : WallpaperService() {
	override fun onCreateEngine(): Engine = WallpaperEngine()

	private inner class WallpaperEngine : WallpaperService.Engine() {
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
			val webView = WebView(pres.context)

			val assetLoader = WebViewAssetLoader.Builder()
				.addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this@RawpWallpaperService))
				.build()

			webView.webViewClient = object : WebViewClient() {
				override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
					return assetLoader.shouldInterceptRequest(request.url)
				}
			}

			webView.settings.javaScriptEnabled = true
			webView.loadUrl("https://appassets.androidplatform.net/assets/index.html")

			pres.setContentView(webView)
			pres.show()
		}

		override fun onTouchEvent(event: MotionEvent?) {
			if (event?.action == MotionEvent.ACTION_DOWN) {
			}
		}
	}
}
