package com.example.ashwin.hajeri;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashwin.hajeri.dbHelper.DatabaseHelper;

import java.io.File;

public class ImportCSV extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_PICK_FILE_TO_SAVE_INTERNAL = 99;
    // FileDialog fileDialog;
    String selectedFile;

    Button bChooseCSV, bImportCSV;
    TextView tvChosenFile;

    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_csv);

        db = new DatabaseHelper(this);

        bChooseCSV = (Button) findViewById(R.id.bChooseCSV);
        bImportCSV = (Button) findViewById(R.id.bImportCSV);
        tvChosenFile = (TextView) findViewById(R.id.tvChosenFile);

        bChooseCSV.setOnClickListener(this);
        bImportCSV.setOnClickListener(this);

/*
        File mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
        fileDialog = new FileDialog(this, mPath);
        fileDialog.setFileEndsWith(".csv");
        fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
            public void fileSelected(File file) {
                Log.d(getClass().getName(), "selected file " + file.toString());
                selectedFile = file.toString();
            }
        });
        //fileDialog.addDirectoryListener(new FileDialog.DirectorySelectedListener() {
        //  public void directorySelected(File directory) {
        //      Log.d(getClass().getName(), "selected dir " + directory.toString());
        //  }
        //});
        //fileDialog.setSelectDirectoryOption(false);
        // fileDialog.showDialog();
*/
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.bChooseCSV:
                //fileDialog.showDialog();

                /*
                Intent fileExploreIntent = new Intent(
                        FileBrowserActivity.INTENT_ACTION_SELECT_DIR,
                        null,
                        this,
                        FileBrowserActivity.class
                );
                startActivityForResult(
                        fileExploreIntent,
                        REQUEST_CODE_PICK_FILE_TO_SAVE_INTERNAL
                );
                */

//                Intent intent = new Intent(this, FileExplore.class);
//                startActivityForResult(intent, REQUEST_CODE_PICK_FILE_TO_SAVE_INTERNAL);
                Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                fileIntent.setType("*/*"); // intent type to filter application based on your requirement
                startActivityForResult(fileIntent, REQUEST_CODE_PICK_FILE_TO_SAVE_INTERNAL);

                break;

            case R.id.bImportCSV:
                db.parseAndSaveCSV(selectedFile);
                Toast.makeText(this, "Import successful.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_PICK_FILE_TO_SAVE_INTERNAL) {
            if (resultCode == RESULT_OK) {
//                String newDir = data.getStringExtra(
//                        FileBrowserActivity.returnDirectoryParameter);
//
//                Toast.makeText(
//                        this,
//                        "Received path from file browser:" + newDir,
//                        Toast.LENGTH_LONG
//                ).show();

                // String FilePath = data.getData().getPath();

                String FILE = Environment.getExternalStorageDirectory().getAbsolutePath();

                Uri uri = data.getData();

                String Fpath = uri.getPath();
                File file = new File(Fpath);
                String filename = file.toString();

                selectedFile = filename.split(":")[1];

                selectedFile = FILE + File.separator + selectedFile;

                tvChosenFile.setText(selectedFile);
                Toast.makeText(this, selectedFile, Toast.LENGTH_SHORT).show();


            } else {//if(resultCode == this.RESULT_OK) {
                Toast.makeText(
                        this,
                        "Received NO result from file browser",
                        Toast.LENGTH_LONG)
                        .show();
            }//END } else {//if(resultCode == this.RESULT_OK) {
        }//if (requestCode == REQUEST_CODE_PICK_FILE_TO_SAVE_INTERNAL) {
        super.onActivityResult(requestCode, resultCode, data);
    }


//    public static class FileDialog {
//        private static final String PARENT_DIR = "..";
//        private final String TAG = getClass().getName();
//        private final Activity activity;
//        private String[] fileList;
//        private File currentPath;
//        private ListenerList<FileSelectedListener> fileListenerList = new ListenerList<FileDialog.FileSelectedListener>();
//        private ListenerList<DirectorySelectedListener> dirListenerList = new ListenerList<FileDialog.DirectorySelectedListener>();
//        private boolean selectDirectoryOption;
//        private String fileEndsWith;
//
//        /**
//         * @param activity
//         * @param path
//         */
//        public FileDialog(Activity activity, File path) {
//            this.activity = activity;
//            if (!path.exists()) path = Environment.getExternalStorageDirectory();
//            loadFileList(path);
//        }
//
//        /**
//         * @return file dialog
//         */
//        public Dialog createFileDialog() {
//            Dialog dialog = null;
//            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//
//            builder.setTitle(currentPath.getPath());
//            if (selectDirectoryOption) {
//                builder.setPositiveButton("Select directory", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.d(TAG, currentPath.getPath());
//                        fireDirectorySelectedEvent(currentPath);
//                    }
//                });
//            }
//
//            builder.setItems(fileList, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    String fileChosen = fileList[which];
//                    File chosenFile = getChosenFile(fileChosen);
//                    if (chosenFile.isDirectory()) {
//                        loadFileList(chosenFile);
//                        dialog.cancel();
//                        dialog.dismiss();
//                        showDialog();
//                    } else fireFileSelectedEvent(chosenFile);
//                }
//            });
//
//            dialog = builder.show();
//            return dialog;
//        }
//
//        public void addFileListener(FileSelectedListener listener) {
//            fileListenerList.add(listener);
//        }
//
//        public void removeFileListener(FileSelectedListener listener) {
//            fileListenerList.remove(listener);
//        }
//
//        public void setSelectDirectoryOption(boolean selectDirectoryOption) {
//            this.selectDirectoryOption = selectDirectoryOption;
//        }
//
//        public void addDirectoryListener(DirectorySelectedListener listener) {
//            dirListenerList.add(listener);
//        }
//
//        public void removeDirectoryListener(DirectorySelectedListener listener) {
//            dirListenerList.remove(listener);
//        }
//
//        /**
//         * Show file dialog
//         */
//        public void showDialog() {
//            createFileDialog().show();
//        }
//
//        private void fireFileSelectedEvent(final File file) {
//            fileListenerList.fireEvent(new ListenerList.FireHandler<FileSelectedListener>() {
//                public void fireEvent(FileSelectedListener listener) {
//                    listener.fileSelected(file);
//                }
//            });
//        }
//
//        private void fireDirectorySelectedEvent(final File directory) {
//            dirListenerList.fireEvent(new ListenerList.FireHandler<DirectorySelectedListener>() {
//                public void fireEvent(DirectorySelectedListener listener) {
//                    listener.directorySelected(directory);
//                }
//            });
//        }
//
//        private void loadFileList(File path) {
//            this.currentPath = path;
//            List<String> r = new ArrayList<String>();
//            if (path.exists()) {
//                if (path.getParentFile() != null) r.add(PARENT_DIR);
//                FilenameFilter filter = new FilenameFilter() {
//                    public boolean accept(File dir, String filename) {
//                        File sel = new File(dir, filename);
//                        if (!sel.canRead()) return false;
//                        if (selectDirectoryOption) return sel.isDirectory();
//                        else {
//                            boolean endsWith = fileEndsWith != null ? filename.toLowerCase().endsWith(fileEndsWith) : true;
//                            return endsWith || sel.isDirectory();
//                        }
//                    }
//                };
//                String[] fileList1 = path.list(filter);
//                for (String file : fileList1) {
//                    r.add(file);
//                }
//            }
//            fileList = (String[]) r.toArray(new String[]{});
//        }
//
//        private File getChosenFile(String fileChosen) {
//            if (fileChosen.equals(PARENT_DIR)) return currentPath.getParentFile();
//            else return new File(currentPath, fileChosen);
//        }
//
//        public void setFileEndsWith(String fileEndsWith) {
//            this.fileEndsWith = fileEndsWith != null ? fileEndsWith.toLowerCase() : fileEndsWith;
//        }
//
//        public interface FileSelectedListener {
//            void fileSelected(File file);
//        }
//
//        public interface DirectorySelectedListener {
//            void directorySelected(File directory);
//        }
//    }
//
//    static class ListenerList<L> {
//        private List<L> listenerList = new ArrayList<L>();
//
//        public void add(L listener) {
//            listenerList.add(listener);
//        }
//
//        public void fireEvent(FireHandler<L> fireHandler) {
//            List<L> copy = new ArrayList<L>(listenerList);
//            for (L l : copy) {
//                fireHandler.fireEvent(l);
//            }
//        }
//
//        public void remove(L listener) {
//            listenerList.remove(listener);
//        }
//
//        public List<L> getListenerList() {
//            return listenerList;
//        }
//
//        public interface FireHandler<L> {
//            void fireEvent(L listener);
//        }
//    }
//

}
