package com.darotapp.cornflix.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darotapp.cornflix.R
import com.darotapp.cornflix.adapters.SwipeAdapter
import com.darotapp.cornflix.data.viewmodel.MovieViewModel
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.ShakeDetector.ShakeListener
import kotlinx.android.synthetic.main.fragment_landing.*
import kotlinx.android.synthetic.main.swipe_recycler_item.*
import top.defaults.drawabletoolbox.DrawableBuilder


/**
 * A simple [Fragment] subclass.
 */
class LandingFragment : Fragment() {

    val adapter:SwipeAdapter? = null
    var movieViewModel:MovieViewModel?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        
        return inflater.inflate(R.layout.fragment_landing, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)





        //set recyclerView Layout
        setRecyclerViewLayout()
        //Swipe listener for left swipe(to delete)
        swipeOnItemTouchHelper()





    }

    /**Not in use**/
    private fun customDrawable() {
        val draw = DrawableBuilder()
            .rectangle()
            .rounded()
            .solidColor(resources.getColor(R.color.colorPrimary))
            .gradientColors(resources.getColor(R.color.colorPrimary), resources.getColor(R.color.darkGreen), null)
            .solidColorPressed(resources.getColor(R.color.darkGreen))
            .build()

        btn.background = draw
    }

    private fun setRecyclerViewLayout() {
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = SwipeAdapter()

    }

    private fun swipeOnItemTouchHelper() {
        //Swipe listener for left swipe(to delete)
        ItemTouchHelper(object :ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val action = LandingFragmentDirections.toFavouriteMovies()
                Navigation.findNavController(recycler_view).navigate(action)
            }

        }).attachToRecyclerView(recycler_view)

        ItemTouchHelper(object :ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                Toast.makeText(context, "swiped right", Toast.LENGTH_LONG).show()
                val action = LandingFragmentDirections.toAllMovies()
                Navigation.findNavController(recycler_view).navigate(action)

            }

        }).attachToRecyclerView(recycler_view)

    }

}
