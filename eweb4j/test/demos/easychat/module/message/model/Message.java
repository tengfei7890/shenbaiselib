package test.demos.easychat.module.message.model;

import java.util.Date;

import test.demos.easychat.common.user.model.User;

import com.cfuture08.eweb4j.orm.config.annotation.Table;
import com.cfuture08.util.JsonConverter;

@Table("t_message")
public class Message {
	public Message(String content){
		this.content = content;
	}
	
	
	private long id;//
	private User sender;// 消息发送者
	private User receiver;// 消息接收者
	private String content;// 消息内容
	private Date sendTime;// 消息发送时间

	
	
	public Message(){
		
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	
	public String toString(){
		return JsonConverter.convert(this);
	}
}
