package net.kamradtfamily.winecheeseapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val KEY_REPOSITORY_TYPE = "repository_type"
        fun intentFor(context: Context, type: WineCheesePostRepository.Type): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(KEY_REPOSITORY_TYPE, type.ordinal)
            return intent
        }
    }

    private val model: WineCheeseViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
            ): T {
                val repoTypeParam = intent.getIntExtra(KEY_REPOSITORY_TYPE, 0)
                val repoType = WineCheesePostRepository.Type.values()[repoTypeParam]
                val repo = ServiceLocator.instance(this@MainActivity)
                        .getRepository(repoType)
                @Suppress("UNCHECKED_CAST")
                return WineCheeseViewModel(repo, handle) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}