package com.example.quoto.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cheezycode.notesample.models.NoteRequest
import com.cheezycode.notesample.models.NoteResponse
import com.example.quoto.NetworkResult
import com.example.quoto.api.NoteApi
import com.example.quoto.models.UserResponse
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NoteRepo @Inject constructor(private val noteApi: NoteApi) {
    private val noteLiveData = MutableLiveData<NetworkResult<List<NoteResponse>>>()
    val notesLivedata: LiveData<NetworkResult<List<NoteResponse>>>
        get() = noteLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLivedata: LiveData<NetworkResult<String>>
        get() = _statusLiveData

    suspend fun getNotes() {
        noteLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.getNote()
        if (response.isSuccessful && response.body() != null) {
            noteLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            noteLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            noteLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }

    }

    suspend fun createNote(noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.createNote(noteRequest)
        responseHandle(response, "note created!!")
    }

    suspend fun deleteNote(noteId: String) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.deleteNote(noteId)
        responseHandle(response, "note deleted!!")
    }

    suspend fun updateNote(noteId: String, noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.updateNote(noteId, noteRequest)
        responseHandle(response, "note updated!!")
    }

    private fun responseHandle(response: Response<NoteResponse>, msg: String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(msg))
        } else {
            _statusLiveData.postValue(NetworkResult.Error("something went wrong"))
        }
    }
}