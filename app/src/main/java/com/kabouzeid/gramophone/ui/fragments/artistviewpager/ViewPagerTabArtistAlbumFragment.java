package com.kabouzeid.gramophone.ui.fragments.artistviewpager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.kabouzeid.gramophone.R;
import com.kabouzeid.gramophone.adapter.AlbumAdapter;
import com.kabouzeid.gramophone.comparator.AlbumAlphabeticComparator;
import com.kabouzeid.gramophone.interfaces.KabViewsDisableAble;
import com.kabouzeid.gramophone.loader.ArtistAlbumLoader;
import com.kabouzeid.gramophone.misc.AppKeys;
import com.kabouzeid.gramophone.model.Album;
import com.kabouzeid.gramophone.ui.activities.AlbumDetailActivity;
import com.melnykov.fab.FloatingActionButton;

import java.util.Collections;
import java.util.List;

/**
 * Created by karim on 04.01.15.
 */
public class ViewPagerTabArtistAlbumFragment extends AbsViewPagerTabArtistListFragment {
    private FloatingActionButton fab;

    @Override
    protected ListAdapter getAdapter() {
        List<Album> albums = ArtistAlbumLoader.getArtistAlbumList(getActivity(), getArtistId());
        Collections.sort(albums, new AlbumAlphabeticComparator());
        //ListAdapter adapter = new AlbumAdapter(getActivity(), albums);
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Album album = (Album) parent.getItemAtPosition(position);
                View albumArtView = view.findViewById(R.id.album_art);

                openAlbumDetailsActivityIfPossible(album, albumArtView);
            }
        });
        setColumns(getResources().getInteger(R.integer.grid_columns));
        //return adapter;
        return null;
    }

    @SuppressWarnings("unchecked")
    private void openAlbumDetailsActivityIfPossible(Album album, View albumArtForTransition) {
        if (areParentActivitiesViewsEnabled()) {
            disableViews();
            disableParentActivitiesViews();

            final Intent intent = new Intent(getActivity(), AlbumDetailActivity.class);
            intent.putExtra(AppKeys.E_ALBUM, album.id);

            final ActivityOptionsCompat activityOptions;
            if (fab != null && albumArtForTransition != null) {
                activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        Pair.create(albumArtForTransition, getString(R.string.transition_album_cover)),
                        Pair.create((View) fab, getString(R.string.transition_fab))
                );
            } else {
                activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity());
            }
            ActivityCompat.startActivity(getActivity(), intent, activityOptions.toBundle());
        }
    }

    private void disableParentActivitiesViews() {
        if (getActivity() instanceof KabViewsDisableAble) {
            ((KabViewsDisableAble) getActivity()).disableViews();
        }
    }

    private boolean areParentActivitiesViewsEnabled() {
        return !(getActivity() instanceof KabViewsDisableAble) || ((KabViewsDisableAble) getActivity()).areViewsEnabled();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
    }
}
