# Carousel
無限輪播廣告頁效果
# 函式功能
1.Carousel        - 本體<br>
2.CarouselAdapter - 內容物的切換元件<br>
3.ItemData        - 內容物的資料來源<br>

# 簡易範例
```
Carousel carousel = findViewById(R.id.carousel);
carousel.setListData(dataList);
carousel.setOnActionListener(new Carousel.OnActionListener() {
  @Override
    public void onClickItem(ItemData item) {
      sendToast(item.getUrl());
    }
});
carousel.init();
```
# 實作功能
1.無限輪播<br> 
2.載入網路圖片<br> 
3.自動換頁<br> 
4.資源檔替換小白點<br> 
# 使用函式庫
[okhttp](https://square.github.io/okhttp/)<br> 
[Glide](https://github.com/bumptech/glide)

# 演示
此演示為3個Carousel的演示<br> 
1.預設<br> 
2.使用資源檔替換小白點<br> 
3.關閉顯示小白點<br> 

![image](https://github.com/ChenNick0401/Carousel/blob/main/carousel.gif?raw=true)
