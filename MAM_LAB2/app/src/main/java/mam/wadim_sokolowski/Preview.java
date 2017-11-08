package mam.wadim_sokolowski;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class Preview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera camera;

    Preview(Context context) {
        super(context);
        mHolder = getHolder();

        mHolder.addCallback(this);

        //musi być mimo że jest w dokumentacji jest deprecated
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {

        camera = Camera.open();
        //Toast.makeText(this.getContext(), "surfaceCreated", Toast.LENGTH_LONG).show();
        try {
            camera.setPreviewDisplay(holder);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        //Toast.makeText(this.getContext(), "surfaceDestroyed", Toast.LENGTH_LONG).show();
        camera = null;

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        Camera.Parameters params = camera.getParameters();
        List<Camera.Size> allSizes = params.getSupportedPictureSizes();
        Camera.Size size = allSizes.get(0); // get top size
        for (int i = 0; i < allSizes.size(); i++) {
            if (allSizes.get(i).width > size.width)
                size = allSizes.get(i);
        }

        params.setPictureSize(size.width, size.height);

        camera.setParameters(params);
        camera.startPreview();
    }
}