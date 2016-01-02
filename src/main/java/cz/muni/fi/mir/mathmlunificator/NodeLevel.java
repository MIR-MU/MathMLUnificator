/*
 * Copyright 2015 Michal Růžička.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.muni.fi.mir.mathmlunificator;

import java.util.Objects;

/**
 * @author Michal Růžička
 */
public class NodeLevel<MajorLevel, MinorLevel> {

    public MajorLevel major;
    public MinorLevel minor;

    public NodeLevel(MajorLevel major, MinorLevel minor) {
        this.major = major;
        this.minor = minor;
    }

    @Override
    public String toString() {
        return major + "." + minor;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.major);
        hash = 97 * hash + Objects.hashCode(this.minor);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NodeLevel<?, ?> other = (NodeLevel<?, ?>) obj;
        if (!Objects.equals(this.major, other.major)) {
            return false;
        }
        if (!Objects.equals(this.minor, other.minor)) {
            return false;
        }
        return true;
    }

}
