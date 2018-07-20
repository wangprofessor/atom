package com.creative.atom.data.json;

import com.creative.atom.action.common.CallbackException;
import com.creative.atom.action.common.IPull;
import com.creative.atom.action.json.IJsonCreator;
import com.creative.atom.action.json.IJsonPuller;
import com.creative.atom.action.json.JsonPuller;
import com.creative.atom.action.json.JsonRequest;
import com.creative.atom.action.json.JsonResponse;
import com.creative.atom.data.Molecule;
import com.creative.atom.data.node.ArraySubCreator;
import com.creative.atom.data.node.NodesCreator;
import com.creative.atom.data.node.ValueRecombine;
import com.creative.atom.data.node.json.JsonNodeHolder;
import com.creative.atom.data.node.object.IObjectNode;
import com.creative.atom.data.node.object.Nodes;
import com.creative.atom.data.node.object.ObjectNodeHolder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangshouchao on 2017/12/27.
 */

@Nodes
public class JsonMolecule <T extends JsonMolecule> extends Molecule {
    private static final String TAG = "JsonMolecule";

    private static final JsonPuller sJsonPuller = new JsonPuller();

    public static void init(IJsonPuller jsonPuller) {
        JsonPuller.init(jsonPuller);
    }

    public static void open() {
        sJsonPuller.open();
    }

    public static void close() {
        sJsonPuller.close();
    }

    public final String tagName;
    public final JsonMolecule input;

    private boolean mInitParent = false;

    protected final HashMap<String, String> mSignMap = new HashMap<>();

    public JsonMolecule() {
        this(null, null);
    }

    public JsonMolecule(JsonMolecule input) {
        this(input, null);
    }

    public JsonMolecule(JsonMolecule input, String tagName) {
        super();
        this.input = input;
        this.tagName = tagName;
    }

    /*以下是自动拉取接口，需要子类覆写*/

    protected boolean inputCanAutoPull() {
        return false;
    }

    protected boolean inputUsePage() {
        return false;
    }

    protected void setInputOffset(long offset) {

    }

    protected int getInputLimit() {
        return 0;
    }

    protected int outputListSize() {
        return 0;
    }

    public long getOffset() {
        return 0;
    }

    public boolean hasRemaining() {
        int outputListSize = outputListSize();
        int inputLimit = input.getInputLimit();
        return input.inputCanAutoPull() && outputListSize >= inputLimit;
    }

    /*自动拉取接口结束*/

    protected boolean isDebug() {
        return false;
    }

    protected IDebug getDebug() {
        return null;
    }

    protected String getUrl() {
        return null;
    }

    protected long getErrorCode(){
        return 0;
    }

    protected void dealWithError(CallbackException callbackException) {

    }

    protected boolean isSuccess() {
        return true;
    }

    protected void initSignMap() {
    }

    protected String onRemapSignKey(String originKey) {
        return originKey;
    }

    private void remapSignKeys() {
        HashMap<String, String> remapKeys = new HashMap<>();
        HashMap<String, String> remapValues = new HashMap<>();
        for (Map.Entry<String, String> entry : mSignMap.entrySet()) {
            String originKey = entry.getKey();
            String value = entry.getValue();
            String newKey = onRemapSignKey(originKey);
            if (!originKey.equals(newKey)) {
                remapKeys.put(originKey, newKey);
                remapValues.put(originKey, value);
            }
        }
        for (Map.Entry<String, String> entry : remapValues.entrySet()) {
            String originKey = entry.getKey();
            String value = entry.getValue();
            String newKey = remapKeys.get(originKey);
            mSignMap.remove(originKey);
            mSignMap.put(newKey, value);
        }
    }

    public void addSignData(JSONObject jsonObject) {
        addSignDataInner(jsonObject);
        traverseSubValue(false, false, new ITraverseGetValue() {
            @Override
            public Object onGetValue(Molecule molecule, Object parent, String name, Object sub, Class<?> clazz, IObjectNode node) {
                if (!JsonMolecule.class.isAssignableFrom(clazz)) {
                    return sub;
                }
                JSONObject subJsonObject = jsonObject.getJSONObject(name);
                if (subJsonObject == null) {
                    return sub;
                }
                JsonMolecule subMolecule = (JsonMolecule) sub;
                subMolecule.addSignData(subJsonObject);
                return sub;
            }
        });
    }

    private void addSignDataInner(JSONObject jsonObject) {
        initSignMap();
        if (mSignMap.size() != 0) {
            remapSignKeys();
            ValueRecombine.recombine(mSignMap, jsonObject);
        }
    }

    @Override
    public JsonMolecule getRootParent() {
        return (JsonMolecule) super.getRootParent();
    }

    @Override
    public JsonMolecule getParent() {
        return (JsonMolecule) super.getParent();
    }

    public HashMap<String, String> getHeader() {
        return null;
    }

    protected ArraySubCreator getSubCreator() {
        return null;
    }

    public void pull(IPull<T> callback) {
        pull(true, false, callback);
    }

    public void pull(boolean autoPull, IPull<T> callback) {
        pull(true, autoPull, callback);
    }

    protected void pullInner(boolean root, IPull<T> callback) {
        pull(root, true, callback);
    }

    public void pull(boolean root, boolean autoPull, IPull<T> callback) {
        if (checkDebug(callback)) {
            return;
        }

        String url = input.getUrl();
        if (url == null) {
            throw new RuntimeException("url is null");
        }

        IJsonCreator jsonCreator = new IJsonCreator() {
            @Override
            public JSONObject createJson() {
                input.tryInitParent();
                tryInitParent();

                JSONObject jsonObject = new JSONObject();
                JsonMolecule inputMolecule;
                if (root) {
                    inputMolecule = input.getRootParent();
                } else {
                    inputMolecule = JsonMolecule.this;
                }
                inputMolecule.toJson(jsonObject);
                inputMolecule.addSignData(jsonObject);

                return jsonObject;
            }
        };

        sJsonPuller.pull(new JsonRequest(input.getUrl(), jsonCreator, input.getHeader()), new PullCallback(root, autoPull, callback));
    }

    private boolean checkDebug(IPull<T> callback) {
        if (isDebug()) {
            IDebug debug = getDebug();
            if (debug != null) {
                CallbackException exception = new CallbackException();
                boolean isDebugError = debug.isDebugError(exception);
                JsonMolecule debugOutput = debug.getDebugOutput();
                if (isDebugError) {
                    callback.onFailure(exception);
                } else if (debugOutput != null) {
                    callback.onGet((T) debugOutput, IPull.SOURCE_NET);
                    Runnable debugAction = debug.getDebugAction();
                    if (debugAction != null) {
                        debugAction.run();
                    }
                }
                return true;
            }
        }
        return false;
    }

    private void tryInitParent() {
        if (mInitParent) {
            return;
        }
        mInitParent = true;
        initParent();
    }

    protected void initParent() {
        // 覆写该方法，调用addParent，不能直接在构造方法中addParent
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        toJson(jsonObject);
        return jsonObject;
    }

    public void toJson(JSONObject jsonObject) {
        recombineTo(jsonObject);
    }

    public JsonMolecule fromJson(JSONObject jsonObject) {
        HashMap<Class<?>, ArraySubCreator> subCreatorMap = getSubCreatorMap();
        if (subCreatorMap.size() == 0) {
            recombineFrom(jsonObject, null);
        } else {
            recombineFrom(jsonObject, subCreatorMap);
        }
        return this;
    }

    private HashMap<Class<?>, ArraySubCreator> getSubCreatorMap() {
        final HashMap<Class<?>, ArraySubCreator> subCreatorMap = new HashMap<>();
        Class<?> selfClazz = getClass();
        ArraySubCreator arraySubCreator = getSubCreator();
        if (arraySubCreator != null) {
            subCreatorMap.put(selfClazz, arraySubCreator);
        }
        traverseSubValue(false, true, new ITraverseGetValue() {
            @Override
            public Object onGetValue(Molecule molecule, Object parent, String name, Object sub, Class<?> clazz, IObjectNode node) {
                Class<?> componentType = clazz.getComponentType();
                if (!(clazz.isArray() && JsonMolecule.class.isAssignableFrom(componentType))) {
                    return sub;
                }

                JsonMolecule jsonMolecule = (JsonMolecule) sValueCreator.createMapValue(componentType, null);

                ArraySubCreator subSubCreator = jsonMolecule.getSubCreator();
                if (subSubCreator == null) {
                    return sub;
                }
                subCreatorMap.put(componentType, subSubCreator);
                return sub;
            }
        });
        return subCreatorMap;
    }

    protected static class JsonArraySubCreator extends ArraySubCreator {
        public JsonArraySubCreator(String tagName) {
            super(NodesCreator.getNodeHolder(Object.class).getNodeCreator(), new JsonNodeHolder().getValueGet(), tagName);
        }

        @Override
        public JsonArraySubCreator setNext(ArraySubCreator next) {
            return (JsonArraySubCreator) super.setNext(next);
        }
    }

    private class PullCallback implements IPull<JsonResponse> {
        private final IPull<T> callback;
        private final boolean root;
        private final boolean autoPull;

        private PullCallback(boolean root, boolean autoPull, IPull<T> callback) {
            this.root = root;
            this.autoPull = autoPull;
            this.callback = callback;
        }

        @Override
        public void onGet(JsonResponse data, int getType) {
            JsonMolecule output;
            JsonMolecule rootParent = getRootParent();
            if (root) {
                output = rootParent;
            } else {
                output = JsonMolecule.this;
            }

            JSONObject jsonObject = data.jsonObject;

            try {
                output.fromJson(jsonObject);
            } catch (Exception e) {
                callback.onFailure(new CallbackException("json error", "json molecule"));
                return;
            }

            T outputSelf = (T) JsonMolecule.this;
            JsonMolecule input = outputSelf.input;
            if (output.isSuccess() && outputSelf.isSuccess()) {
                callback.onGet(outputSelf, IPull.SOURCE_NET);
                if (input.inputCanAutoPull() && autoPull) {
                    boolean hasRemaining = hasRemaining();
                    if (hasRemaining) {
                        if (input.inputUsePage()) {
                            input.setInputOffset(input.getOffset() + 1);
                        } else {
                            input.setInputOffset(outputSelf.getOffset() + 1);
                        }
                        outputSelf.pullInner(root, callback);
                    }
                }
            } else {
                CallbackException callbackException = new CallbackException("protocol error", "json molecule");
                if (!output.isSuccess()) {
                    callbackException.obj = output;
                } else if (!outputSelf.isSuccess()) {
                    callbackException.obj = outputSelf;
                }
                callbackException.code = rootParent.getErrorCode();
                rootParent.dealWithError(callbackException);
                callback.onFailure(callbackException);
            }
        }

        @Override
        public void onFailure(CallbackException e) {
            callback.onFailure(e);
        }

        @Override
        public void onTooMuch() {
            callback.onFailure(new CallbackException("too much", "json pull"));
        }
    }

    public interface IDebug {
        boolean isDebugError(CallbackException exception);
        JsonMolecule getDebugOutput();
        Runnable getDebugAction();
    }
}
