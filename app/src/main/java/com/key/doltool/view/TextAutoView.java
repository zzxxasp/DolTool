package com.key.doltool.view;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

//自定义文本标签，自动换行
public class TextAutoView extends TextView {
	private TextPaint mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	public TextAutoView(Context context) {
		this(context,null);
	}
	
	public TextAutoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public TextAutoView(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}

	private void init() {
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLACK); 
		mPaint.setStyle(Style.FILL_AND_STROKE);
//		mPaint.setTextSize(getTextSize());
		mPaint.setTextSize(16f);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		//draw backgroud
		canvas.drawColor(0xfff3f5e9);
		
		//draw text
		FontMetrics fm = mPaint.getFontMetrics();
		
		float baseline = fm.descent - fm.ascent; 
		float x = 0;
		//由于系统基于字体的底部来绘制文本，所有需要加上字体的高度
		float y =  baseline;
		String txt = getText().toString();		
		//文本自动换行
		String[] texts = autoSplit(txt, mPaint, getWidth()-15);		
		
		for(String text : texts) { 
			canvas.drawText(text, x, y, mPaint);
			y += baseline + fm.leading;
		}
	}
	
	private String[] autoSplit(String content, TextPaint p, float width) {
		int length = content.length();
		float textWidth = p.measureText(content);
		if(textWidth <= width) {
			return new String[]{content};
		}
		
		int start = 0, end = 1, i = 0;
		int lines = (int) Math.ceil(textWidth / width); //计算行数
		String[] lineTexts = new String[lines];
		while(start < length) {
			if(p.measureText(content, start, end) > width) {
				lineTexts[i++] = (String) content.subSequence(start, end);
				start = end;
			}
			if(end == length) { 
				lineTexts[i] = (String) content.subSequence(start, end);
				break;
			}
			end += 1;
		}
		return lineTexts;
	}
}