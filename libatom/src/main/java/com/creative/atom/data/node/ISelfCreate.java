package com.creative.atom.data.node;

import com.creative.atom.data.Molecule;

public interface ISelfCreate {
    Molecule selfCreate();
    Object selfCreateArray(int length);
}
