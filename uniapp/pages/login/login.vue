<template>
	<view class="container" :style="{'height':height + 'px'}">
		<view class="title">
			<text style="margin-left: 15px;">testGpt</text>
		</view>
		<view class="loginbody">
			<view>
				<form class="formbody" @submit="login">
					<view class="bodys">
						<view><text>賬號:</text></view>
						<input name="name" v-model="name" placeholder="請輸入賬號" style="background-color: white;width: 80%;margin: auto;border-radius: 3px;border: solid 2px;"/>
					</view>
					<view class="bodys">
						<view><text>密碼:</text></view>
						<input name="password" placeholder="請輸入密碼" password style="background-color: white;width: 80%;margin: auto;border-radius: 3px;border: solid 2px;"/>
					</view>
					<view style="margin: auto;margin-top: 30px;">
						<button form-type="submit" size="mini" type="primary" >登錄</button>
					</view>
				</form>
			</view>
		</view>
	</view>
</template>

<script>
	
	const BASE_PATH = 'http://localhost:8082/login'
	const INDEX = 'http://localhost:8081/index/index'
	
	
	export default {
		data() {
			return {
				height:"",
				name:""
			};
		},
		onLoad() {
			var that = this;
			uni.getSystemInfo({
				success: function (res) {
					that.height = res.windowHeight;
					console.log(that.height)
				}
			});
			this.tokenLogin();
		},
		methods: {
			login:function(e){
				let that = this;
				let data = {
					name:e.detail.value.name,
					password:e.detail.value.password
				};
				uni.request({
					url:BASE_PATH + '/login',
					method:'POST',
					data:data,
					header:{
						
					},
					success(res) {
						if (res.data.flag == true){
							uni.setStorageSync('token',res.data.message)
							uni.showToast({
								title:'登陸成功,正在跳轉',
								duration:2000,
							})
							uni.navigateTo({
								url:'/pages/index/index?name=' + that.name,
								
							})
						}else{
							uni.showModal({
								title:'登陸失敗',
								content:'賬號或密碼錯誤',
								showCancel:false,
								success(res) {
									return
								}
							})
						}
					}
				})
			},
			tokenLogin(){
				let that = this;
				let token = uni.getStorageSync('token');
				if(token != null){
					console.log('token不爲空',token)
				}	
				uni.request({
					url:BASE_PATH + '/getToken',
					data:{
						"token":token
					},
					method:'POST',
					success(res) {
						debugger
						if (res.data.flag == true){
							uni.setStorageSync('token',res.data.message)
							uni.showToast({
								title:'登陸成功,正在跳轉',
								duration:2000,
							})
							uni.navigateTo({
								url:'/pages/index/index?name=' + that.name,
								
							})
						}else{
							uni.showModal({
								title:'登陸失敗',
								content:'登錄已過期請重新登錄',
								showCancel:false,
								success(res) {
									return
								}
							})
						}
					}
				})	
				
			}
		}
		
	}
</script>

<style lang="scss">
	.container{
		display: flex;
		flex-direction: column;
		background-color: snow;
	}
	.title{
		height: 5%;
		background-color: green;
		display: flex;
		align-items: center;
	}
	.loginbody{
		width: 70%;
		height: 500rpx;
		background-color: snow;
		margin: auto;
	}
	.formbody{
		text-align: center;
		
	}
	.bodys{
		margin-top: 80rpx;
		display: flex;
		flex-direction: row;
	}
</style>
