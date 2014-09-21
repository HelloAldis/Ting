
package com.kindazrael.tingweather.model.descriptor;

import android.content.Context;
import android.view.View;

import com.kindazrael.tingweather.R;

public abstract class WeatherTypeDescriptor {

    /**
     * 修改主页面中城市lablel的样式，样式定义在styles.xml
     * @return
     *        - Style id
     */
    public int getWeatherLocationStyle() {
        return R.style.white_30;
    }
    
    /**
     * 修改主页面中天气信息lablel的样式，样式定义在styles.xml
     * @return
     *        - Style id
     */
    public int getWeatherStatusLabelStyle() {
        return R.style.white_18;
    }
    
    /**
     * 修改主页面中温度lablel的样式，样式定义在styles.xml
     * @return
     *        - Style id
     */
    public int getTemperatureLabelStyle() {
        return R.style.white_80;
    }
    
    /**
     * 修改主页面中底部label的样式，样式定义在styles.xml
     * @return
     *        - Style id
     */
    public int getBottonWeatherLabelStyle() {
        return R.style.white_17;
    }
    
    /**
     * 修改主页面中底部背景的颜色，样式定义在color.xml
     * @return
     */
    public int getBottonBackground() {
        return R.color.bg_main_bottom;
    }
    
    /**
     *获取天气对应的小图标的资源文件
     */
    public abstract int getIcon();
    
    /**
     *获取天气对应的小图标的资源文件显示再citylist界面上
     */
    public abstract int getIconForCityList();

    /**
     *获取主页面的背景颜色，该颜色需要通过xml配置成drawable，具体参考SunnyDescriptor.java.
     */
    public abstract int getBackground();
    
    /**
     *获取主页面的布局视图，可以用代码添加，也可以引入布局文件，具体参考SunnyDescriptor.java.
     */
    public abstract View getDisplayingView(Context context);
    
    /**
     * 检测当前是晚上还是白天，早6点到晚7点是白天
     * 
     * @return
     */
    public boolean isNight() {
		// return DateUtil.isNight();
		return true;
    }

}
