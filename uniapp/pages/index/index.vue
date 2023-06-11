<template>
	<view class="content">
		<view class="menu"><view class="menuicon" @click="showDrawer"></view></view>
		<view>
			<scroll-view :show-scrollbar="true" scroll-y="true" :style="{'height': height + 'px'}" :scroll-top="sctop" class="body1">
				<view v-for="item in gptLog" style="padding-bottom: 15px;">
					<view class="container-wrapper-left" v-if="item.uid == 'gpt'">
						<view class="log">
							Gpt
						</view>
						<view style="margin-top: 5rpx;background-color: black;color: white;margin-left: 15px;border-radius: 2.5px;padding: 5px;">
							<text style="width: 100%;display:inline-block;white-space: pre-wrap; word-wrap: break-word;height: auto;" :selectable="true">{{item.msg}}</text>
						</view>
					</view>
					<view class="container-wrapper-right" v-if="item.uid == 'user'">
						<view class="log" style="background-color: black;color: greenyellow;">
							Cx
						</view>
						<view style="margin-top: 5rpx;background-color: green;color: white;width: 87%;margin-right: 15px;width: auto;border-radius: 2.5px;padding: 5px;">
							<text style="width: 100%;display:inline-block;white-space: pre-wrap; word-wrap: break-word;height: auto;" :selectable="true">{{item.msg}}</text>
						</view>
					</view>
				</view>
			</scroll-view>
		</view>
		<view style="">
			<input class="ipt" confirm-type="search" @confirm="push()" adjust-position placeholder="问问神奇的mmj" v-model="msg"/>
		</view>
		<uni-drawer ref="showRight" mode="left" :mask-click="true"> 
			<scroll-view style="height: 100%;" scroll-y="true">
				<view class="menu" style="margin-right: 25px;width: 100%;">
					<text style="">testGpt</text>
					<view class="menuicon" style="margin-left: 100px;" @click="closeDrawer"></view>				
				</view>
				
			</scroll-view>
		</uni-drawer>
	</view>
</template>

<script>
	var socketOpen = false;
	export default {
		data() {
			return {
				title: 'Hello',
				windowsHeight:"",
				msg:"",
				height:"",
				ws:null,
				timer:"",
				name:"",
				gptLog:[],
				sctop:1,
			}
		},
		onLoad(option) {
			let that = this;
			uni.getSystemInfo({
				success: function (res) {
					that.height = res.windowHeight - 120;
					console.log(that.height)
				}
			});
			this.name = option.name
			this.onOpen();
		},
		updated() {
			this.sctop = 9999;
		},
		methods: {
			onOpen(){
				let token = uni.getStorageSync('token');
				let that = this;
				this.ws = uni.connectSocket({
					url: 'ws://localhost:8082/chat',
					data() {
						return {
							x: '',
							y: ''
						};
					},
					method: 'GET',
					header:{
						'Authentication':token
					},
					success(res) {
						console.log("鏈接打開成功", res)
					},
					fail(err) {
						console.log("出現錯誤",err)
					}
					
				});
				uni.onSocketOpen(function (res) {
					socketOpen = true;
					console.log("鏈接已打開");
				});
				uni.onSocketMessage(function (res){
					let msg = {
						msg:res.data,
						uid:"gpt"
					}
					that.gptLog.push(msg)
					
				})
			
			},
			push(){
				if(socketOpen){
					uni.sendSocketMessage({
						data:this.msg
						
					});
					let msgs = {
						msg:this.msg,
						uid:"user"
					}
					this.gptLog.push(msgs);
					this.msg = "";
					
				}
			},
			heart() {
				let that = this;
				clearInterval(this.timer);
				this.timer = '';
				let msg = {
					"type": "heartbeat",
				}
				this.timer = setInterval(() => {
					 that.sendSocketMessage(JSON.stringify(msg)).then(res => {
						console.log('心跳成功')
					}).catch(res => {
						console.log('发送失败')
					console.log((res))
					 })
							 
				}, 200000)
			},
			
		},	
		
	}
</script>

<style lang="scss">
	.content {
		display: flex;
		flex-direction:column;
	}
	.menu {
		display: flex;
		font-family:Comic Sans MS;
		font-size:14px;
		height: 15px;
		margin-top: 15px;
		margin-left: 15px;
	}
	.menuicon{
		width: 20px;
		height: 2px;
		border-top: 2px solid black;
		border-bottom: 2px solid black;
		background-color: black;
		padding: 2px 0;
		background-clip:content-box;
		margin-top: 5px;
	}
	.body1{
		padding-left: 15px;
		margin-top: 15px;
		padding-top: 15px;
		border-top: solid 1px black;
		width: 90%;
	}
	.ipt{
		width: 90%;
		border: solid 1px black;
		margin: auto;
		border-radius: 5px;
		height: 60rpx;
		margin-top: 5px;
		
	}
	.log{
		    -webkit-text-size-adjust: 100%;
		    font-family: Söhne,ui-sans-serif,system-ui,-apple-system,Segoe UI,Roboto,Ubuntu,Cantarell,Noto Sans,sans-serif,Helvetica Neue,Arial,Apple Color Emoji,Segoe UI Emoji,Segoe UI Symbol,Noto Color Emoji;
		    tab-size: 4;
		    --color-primary: #19c37d;
		    --color-secondary: #715fde;
		    --color-error: #ef4146;
		    --gradient-primary: linear-gradient(90deg,#a29bd4,#989fdd);
		    --text-primary: #202123;
		    --text-default: #353740;
		    --text-secondary: #6e6e80;
		    --text-disabled: #acacbe;
		    --text-error: var(--color-error);
		    color-scheme: light;
		    --tw-bg-opacity: 1;
		    font-size: 1rem;
		    line-height: 1.5rem;
		    border: 0 solid #d9d9e3;
		    box-sizing: border-box;
		    --tw-border-spacing-x: 0;
		    --tw-border-spacing-y: 0;
		    --tw-translate-x: 0;
		    --tw-translate-y: 0;
		    --tw-rotate: 0;
		    --tw-skew-x: 0;
		    --tw-skew-y: 0;
		    --tw-scale-x: 1;
		    --tw-scale-y: 1;
		    --tw-pan-x: ;
		    --tw-pan-y: ;
		    --tw-pinch-zoom: ;
		    --tw-scroll-snap-strictness: proximity;
		    --tw-ordinal: ;
		    --tw-slashed-zero: ;
		    --tw-numeric-figure: ;
		    --tw-numeric-spacing: ;
		    --tw-numeric-fraction: ;
		    --tw-ring-inset: ;
		    --tw-ring-offset-width: 0px;
		    --tw-ring-offset-color: #fff;
		    --tw-ring-color: rgba(59,130,246,.5);
		    --tw-ring-offset-shadow: 0 0 transparent;
		    --tw-ring-shadow: 0 0 transparent;
		    --tw-shadow: 0 0 transparent;
		    --tw-shadow-colored: 0 0 transparent;
		    --tw-blur: ;
		    --tw-brightness: ;
		    --tw-contrast: ;
		    --tw-grayscale: ;
		    --tw-hue-rotate: ;
		    --tw-invert: ;
		    --tw-saturate: ;
		    --tw-sepia: ;
		    --tw-drop-shadow: ;
		    --tw-backdrop-blur: ;
		    --tw-backdrop-brightness: ;
		    --tw-backdrop-contrast: ;
		    --tw-backdrop-grayscale: ;
		    --tw-backdrop-hue-rotate: ;
		    --tw-backdrop-invert: ;
		    --tw-backdrop-opacity: ;
		    --tw-backdrop-saturate: ;
		    --tw-backdrop-sepia: ;
		    position: relative;
		    display: flex;
		    height: 30px;
		    width: 30px;
		    align-items: center;
		    justify-content: center;
		    border-radius: .125rem;
		    padding: .25rem;
		    --tw-text-opacity: 1;
		    color: rgba(255,255,255,var(--tw-text-opacity));
		    background-color: rgb(16, 163, 127);
	}
	.container-wrapper-left{
		display: flex;
		flex-direction: row;
		
	}
	.speech-bubble {
		width: 120px;
		height: 80px;
		background: #5ac4ed;
		position: absolute;	
		left:100px;
		-moz-border-radius: 10px;
		-webkit-border-radius: 10px;
		border-radius: 10px;
	}
	.speech-bubble:before {
		content:"";
		position: absolute;
		width: 0;
		height: 0;
		border-top: 13px solid transparent;
		border-right: 26px solid #5ac4ed;
		border-bottom: 13px solid transparent;
		margin: 13px 0 0 -25px;
	}
	.container-wrapper-right{
		display: flex;
		flex-direction: row-reverse;
		
	}
</style>
