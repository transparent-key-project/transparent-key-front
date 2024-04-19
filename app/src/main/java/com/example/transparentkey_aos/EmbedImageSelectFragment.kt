package com.example.transparentkey_aos

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.transparentkey_aos.databinding.FragmentEmbedImageSelectBinding

class EmbedImageSelectFragment : Fragment() {
    lateinit var binding: FragmentEmbedImageSelectBinding
    private val REQUEST_KEY = "wm_img" // api요청 키
    private lateinit var selectedWatermark: Bitmap
    private var launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { it ->
        setGallery(uri = it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmbedImageSelectBinding.inflate(inflater, container, false)
//        Toast.makeText(context, "Image select fragment", Toast.LENGTH_SHORT).show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launcher.launch("image/*")
    }

    /**
     * 갤러리 실행, selectedWatermark에 bitmap으로 저장
     */
    fun setGallery(uri: Uri?) {
        uri?.let {
            try {
                val inputStream = requireActivity().contentResolver.openInputStream(uri)
                selectedWatermark = BitmapFactory.decodeStream(inputStream)

                // 다음 프래그먼트로 전환
                replaceFragment(EmbedFragment(), selectedWatermark)
            } catch (e: Exception) {
                Log.e("EmbedImageSelectFragment", "Image selection failed", e)
            }
        }
    }

    /**
     * replace fragment
     */
    fun replaceFragment(fragment: Fragment, img: Bitmap) {
        // 데이터 전송
        setFragmentResult(REQUEST_KEY, Bundle().apply {
            putParcelable("wm_img", img)
        })

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentCotainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}