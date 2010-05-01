package com.ribbit.rest

import com.ribbit.rest.exceptions.NotAuthorizedException
import com.ribbit.rest.exceptions.RibbitException
import com.ribbit.rest.oauth.SignedRequest
import com.ribbit.rest.util.Util

/**
 * Created by IntelliJ IDEA.
 * User: jwill
 * Date: Feb 3, 2010
 * Time: 12:00:19 PM
 */
class Folder extends Resource {

    String folderName
    List<FolderResource> files = new ArrayList<FolderResource>()

    public boolean removeFile(String filename) {
        def userId = config.getActiveUserId()
        if (!userId) {
            throw new NotAuthorizedException()
        }

        if (!Util.isValidString(filename)) {
            throw new IllegalArgumentException("filename is required")
        }

        def req = new SignedRequest(config)
        String uri
        if (filename.startsWith(folderName + "/"))
            uri = "media/${config.getDomain()}/${filename}"
        else uri = "media/${config.getDomain()}/${folderName}/${filename}"
        req.delete(uri)

        def file = files.find {(it.id == folderName + "/" + filename) || (it.id == folderName)}
        files.remove(file)

        return true
    }

    public void uploadFile(InputStream stream, String filename) throws RibbitException {

        def userId = config.getActiveUserId()
        if (!userId) {
            throw new NotAuthorizedException()
        }

        def exceptions = []

        if (!Util.isValidString(filename)) {
            exceptions.add("file is required");
        }
        if (stream == null) {
            exceptions.add("An InputStream to a readable file must be supplied")
        }

        if (exceptions.size() > 0) {
            throw new IllegalArgumentException(exceptions.join(";"))
        }
        def req = new SignedRequest(config)
        String uri = "media/${config.getDomain()}/${folderName}/${filename}"
        String result = null
        String serviceResult = req.postFile(stream, uri)

        files.add(new FolderResource(id: filename, associatedApp: config.application, createdBy: config.getActiveUserId()))
    }

    public void uploadFile(File file) throws RibbitException {

        def userId = config.getActiveUserId()
        if (!userId) {
            throw new NotAuthorizedException()
        }

        if (!file) {
            throw new IllegalArgumentException("A readable file must be supplied")
        }

        def req = new SignedRequest(config)
        String uri = "media/${config.getDomain()}/${folderName}/${file.getName()}"
        String result = null
        String serviceResult = req.postFile(file.newInputStream(), uri)

        files.add(new FolderResource(id: file.getName(), associatedApp: config.application, createdBy: config.getActiveUserId()))
    }

    public boolean remove() throws RibbitException {
        def userId = config.getActiveUserId()
        if (!userId) {
            throw new NotAuthorizedException()
        }

        def req = new SignedRequest(config)
        String uri = "media/${config.getDomain()}/${folderName}"
        req.delete(uri)

        return true
    }

    public File downloadFile(FolderResource resource) {
        return downloadFile(resource.getId())
    }

    public File downloadFile(String filename) throws RibbitException {

        def userId = config.getActiveUserId()
        if (!userId) {
            throw new NotAuthorizedException()
        }

        def file = new File(filename)
        def stream = file.newOutputStream()

        def req = new SignedRequest(config)
        String uri = "media/${config.getDomain()}/${folderName}/${filename}"

        String serviceResult = req.getToFile(uri, stream)
        println serviceResult
        return file
    }

    public  String getFileText(String filename)  throws RibbitException  {
        def userId = config.getActiveUserId()
        if (!userId) {
            throw new NotAuthorizedException()
        }

        def req = new SignedRequest(config)
        String uri = "media/${config.getDomain()}/${folderName}/${filename}"
        String result = null;
        String serviceResult = req.get(uri);

        return serviceResult;

    }
}
