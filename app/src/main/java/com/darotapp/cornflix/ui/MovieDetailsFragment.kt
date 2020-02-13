package com.darotapp.cornflix.ui


import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.darotapp.cornflix.R
import com.darotapp.cornflix.model.database.MovieEntity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_movie_details.*

/**
 * A simple [Fragment] subclass.
 */
class MovieDetailsFragment : Fragment() {
    var incomingMovie:MovieEntity?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //Receives arguments from other fragment
        arguments?.let{
            incomingMovie = MovieDetailsFragmentArgs.fromBundle(it).movie
        }

        image.transitionName = incomingMovie?.movieId


        Picasso.get().load(incomingMovie?.movieImage).into(image)
        overView.setText(incomingMovie?.overView)
    }
//
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        recyclerViewAdapter = RecyclerViewAdapter(DummyContent.ITEMS)
//        recyclerViewAdapter.listener = object : RecyclerViewAdapter.ItemClickListener {
//            override fun onItemClickListener(item: DummyContent.DummyItem, imageView: ImageView) {
//                val extras = FragmentNavigatorExtras(
//                    imageView to item.id
//                )
//                val action = RecyclerViewFragmentDirections.navToItemDetailFragment(id = item.id)
//                findNavController().navigate(action, extras)
//            }
//        }
//        view.findViewById<RecyclerView>(R.id.recycler_view).apply {
//            layoutManager = GridLayoutManager(context, columnCount)
//            adapter = recyclerViewAdapter
//        }
//    }

//    private val args: ItemDetailFragmentArgs by navArgs()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_item_detail, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val id = args.id
//        item_image.transitionName = id
//        item_name.text = id
//    }
}
