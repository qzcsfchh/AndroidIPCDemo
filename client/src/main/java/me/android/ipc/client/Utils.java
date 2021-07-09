package me.android.ipc.client;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;

public class Utils {

    public static Bitmap loadBitmap(Context context) {
        Drawable icon = context.getApplicationInfo().loadIcon(context.getPackageManager());
        if (icon == null) return null;
        int width = icon.getIntrinsicWidth();
        int height = icon.getIntrinsicHeight();
        if (width == 0 || height == 0) return null;
        icon.setBounds(0, 0, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        icon.draw(canvas);
        return bitmap;
    }


    public static String dumpBundle(@Nullable Bundle bundle){
        if (bundle == null) {
            return "null";
        }
        return bundle.toString();
    }

    public static void close(Closeable... clos){
        for (Closeable clo : clos) {
            if (clo != null) {
                try {
                    clo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static byte[] parcelable2Bytes(@NonNull Parcelable p){
        Parcel parcel = Parcel.obtain();
        p.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    public static <T> T bytes2Parcelable(final byte[] bytes, final Parcelable.Creator<T> creator) {
        if (bytes == null) return null;
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0);
        T result = creator.createFromParcel(parcel);
        parcel.recycle();
        return result;
    }


    public static byte[] bitmap2Bytes(final Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap bytes2Bitmap(final byte[] bytes) {
        return (bytes == null || bytes.length == 0)
                ? null
                : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }



    public interface BiConsumer<T, U> {

        /**
         * Performs this operation on the given arguments.
         *
         * @param t the first input argument
         * @param u the second input argument
         */
        void accept(T t, U u);

        /**
         * Returns a composed {@code BiConsumer} that performs, in sequence, this
         * operation followed by the {@code after} operation. If performing either
         * operation throws an exception, it is relayed to the caller of the
         * composed operation.  If performing this operation throws an exception,
         * the {@code after} operation will not be performed.
         *
         * @param after the operation to perform after this operation
         * @return a composed {@code BiConsumer} that performs in sequence this
         * operation followed by the {@code after} operation
         * @throws NullPointerException if {@code after} is null
         */
        default BiConsumer<T, U> andThen(BiConsumer<? super T, ? super U> after) {
            Objects.requireNonNull(after);

            return (l, r) -> {
                accept(l, r);
                after.accept(l, r);
            };
        }
    }


    public static final class XData implements Parcelable {
        private byte[] data;
        private String call;
        private String error;

        public byte[] getData() {
            return data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }

        public String getCall() {
            return call;
        }

        public void setCall(String call) {
            this.call = call;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByteArray(this.data);
            dest.writeString(this.call);
            dest.writeString(this.error);
        }

        public void readFromParcel(Parcel source) {
            this.data = source.createByteArray();
            this.call = source.readString();
            this.error = source.readString();
        }

        public XData() {
        }

        protected XData(Parcel in) {
            this.data = in.createByteArray();
            this.call = in.readString();
            this.error = in.readString();
        }

        public static final Parcelable.Creator<XData> CREATOR = new Parcelable.Creator<XData>() {
            @Override
            public XData createFromParcel(Parcel source) {
                return new XData(source);
            }

            @Override
            public XData[] newArray(int size) {
                return new XData[size];
            }
        };
    }
}
