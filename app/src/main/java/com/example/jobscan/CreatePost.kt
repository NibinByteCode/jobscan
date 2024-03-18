package com.example.jobscan

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.jobscan.helpers.BottomNavigationHandler
import com.example.jobscan.models.PostData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class CreatePost : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var bottomNavigationHandler: BottomNavigationHandler
    private lateinit var imageView: ImageView
    private lateinit var editTextContent: EditText
    private lateinit var buttonChooseImage: Button
    private lateinit var buttonPost: Button
    private var imageUri: Uri? = null
    private lateinit var storageRef: StorageReference
    private lateinit var firebaseDB: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.apply {
            title = "Create New Post"
        }
        setContentView(R.layout.activity_create_post)
        bottomNavigationView = findViewById(R.id.nav_view) // Correct initialization
        bottomNavigationHandler = BottomNavigationHandler(this)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            bottomNavigationHandler.onNavigationItemSelected(item.itemId)
        }
        imageView = findViewById(R.id.imageView)
        editTextContent = findViewById(R.id.editTextContent)
        buttonChooseImage = findViewById(R.id.buttonChooseImage)
        buttonPost = findViewById(R.id.buttonPost)

        storageRef = FirebaseStorage.getInstance().reference
        firebaseDB = FirebaseDatabase.getInstance().reference.child("Posts")

        buttonChooseImage.setOnClickListener {
            chooseImage()
        }

        buttonPost.setOnClickListener {
            uploadPost()
        }
    }

    private fun uploadPost() {

        val content = editTextContent.text.toString().trim()

        if (content.isEmpty()) {
            editTextContent.error = "Please enter your post content"
            return
        }

        // Upload image if available
        if (imageUri != null) {
            val uniqueID = UUID.randomUUID().toString() + ".jpg"

            val imageRef = storageRef.child("postImages/${uniqueID}")
            val uploadTask = imageRef.putFile(imageUri!!)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result

                    val post = PostData(
                        postId = "",
                        postContent = content,
                        postImage = downloadUri.toString(),
                        userId = FirebaseAuth.getInstance().currentUser?.uid.toString(),
                        postDate = System.currentTimeMillis(),
                    )
                    val postId = firebaseDB.child("Posts").push().key
                    postId?.let { generatedPostId ->
                        post.postId = generatedPostId
                        val postValues = post.toMap()
                        val childUpdates = HashMap<String, Any>()
                        childUpdates["/$generatedPostId"] = postValues
                        firebaseDB.updateChildren(childUpdates).addOnSuccessListener {
                            setResult(Activity.RESULT_OK)
                            navigateToMainActivity()
                        }.addOnFailureListener { e ->
                            // Handle errors
                        }
                    }
                } else {
                    // Handle failures
                }
            }
        } else {
            // If no image, just upload post content
            val post = PostData(
                postId = "",
                postContent = content,
                userId = FirebaseAuth.getInstance().currentUser?.uid.toString(), // Replace with actual user ID
                postDate = System.currentTimeMillis(),
            )

            val postId = firebaseDB.push().key
            postId?.let { generatedPostId ->
                post.postId = generatedPostId
                val postValues = post.toMap()
                val childUpdates = HashMap<String, Any>()
                childUpdates["/$generatedPostId"] = postValues

                firebaseDB.updateChildren(childUpdates).addOnSuccessListener {
                    setResult(Activity.RESULT_OK)
                    navigateToMainActivity()
                }.addOnFailureListener { e ->
                    // Handle errors
                }
            }
        }
    }


    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finish the current activity
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            imageView.setImageURI(imageUri)
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}