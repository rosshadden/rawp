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

	private inner class MyWebView(context: Context) : WebView(context) {
		override fun onTouchEvent(event: MotionEvent): Boolean {
			println("WHATY IS HAPPENING " + event.action)
//			requestDisallowInterceptTouchEvent(true)
			return super.onTouchEvent(event)
		}
	}

	private inner class RawpApi(val engine: WallpaperEngine) {
		@android.webkit.JavascriptInterface
		fun launch(id: String) {
			val intent = packageManager.getLaunchIntentForPackage(id)
			if (intent != null) {
				startActivity(intent)
			} else {
				println("Nope: $id")
			}
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
//			webView.loadUrl("https://google.com")

			pres.setContentView(view)
			pres.show()
		}

		override fun onTouchEvent(event: MotionEvent) {
			view.dispatchTouchEvent(event)
//			return super.onTouchEvent(event)
		}
	}
}
