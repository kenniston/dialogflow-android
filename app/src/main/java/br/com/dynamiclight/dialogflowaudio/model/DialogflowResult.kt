package br.com.dynamiclight.dialogflowaudio.model

data class DialogflowRequest(val text: String, val email: String, val sessionId: String)

class DialogflowResult()