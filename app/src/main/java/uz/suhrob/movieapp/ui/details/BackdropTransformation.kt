package uz.suhrob.movieapp.ui.details

import android.graphics.*
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class BackdropTransformation(private val radius: Int): BitmapTransformation() {
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update("uz.suhrob.movieapp.ui.details.BackdropTransformation$radius".toByteArray(Key.CHARSET))
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val width = toTransform.width
        val height = toTransform.height
        val bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setHasAlpha(true)
//        setBit
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        drawRoundRect(canvas, paint, width.toFloat(), height.toFloat())
        return bitmap
    }

    private fun drawRoundRect(canvas: Canvas, paint: Paint, width: Float, height: Float) {
        val zero = 0.toFloat()
        val radius1 = radius.toFloat()
        canvas.drawRoundRect(RectF(zero, height-2*radius, 2*radius1, height), radius1, radius1, paint)
        canvas.drawRect(RectF(zero, zero, 2*radius1, height-radius1), paint)
        canvas.drawRect(RectF(radius1, zero, width, height), paint)
    }
}