/*
 * Copyright 2014 Goldman Sachs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gs.collections.impl.bag.immutable;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.primitive.ImmutableBooleanBag;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.partition.bag.PartitionImmutableBag;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.function.PassThruFunction0;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.sorted.mutable.TreeSortedMap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

public class ImmutableEmptyBagTest extends ImmutableBagTestCase
{
    public static final Predicate<String> ERROR_THROWING_PREDICATE = each -> { throw new AssertionError(); };

    public static final Predicates2<String, Class<Integer>> ERROR_THROWING_PREDICATE_2 = new Predicates2<String, Class<Integer>>()
    {
        public boolean accept(String argument1, Class<Integer> argument2)
        {
            throw new AssertionError();
        }
    };

    @Override
    protected ImmutableBag<String> newBag()
    {
        return (ImmutableBag<String>) ImmutableEmptyBag.INSTANCE;
    }

    @Override
    protected int numKeys()
    {
        return 0;
    }

    @Test
    public void testFactory()
    {
        Verify.assertInstanceOf(ImmutableEmptyBag.class, Bags.immutable.of());
    }

    @Test
    @Override
    public void newWith()
    {
        ImmutableBag<String> bag = this.newBag();
        ImmutableBag<String> newBag = bag.newWith("1");
        Assert.assertNotEquals(bag, newBag);
        Assert.assertEquals(newBag.size(), bag.size() + 1);
        ImmutableBag<String> newBag2 = bag.newWith("5");
        Assert.assertNotEquals(bag, newBag2);
        Assert.assertEquals(newBag2.size(), bag.size() + 1);
        Assert.assertEquals(1, newBag2.sizeDistinct());
    }

    @Test
    @Override
    public void select()
    {
        ImmutableBag<String> strings = this.newBag();
        Verify.assertIterableEmpty(strings.select(Predicates.lessThan("0")));
    }

    @Test
    @Override
    public void reject()
    {
        ImmutableBag<String> strings = this.newBag();
        Verify.assertIterableEmpty(strings.reject(Predicates.greaterThan("0")));
    }

    @Override
    public void partition()
    {
        PartitionImmutableBag<String> partition = this.newBag().partition(Predicates.lessThan("0"));
        Verify.assertIterableEmpty(partition.getSelected());
        Verify.assertIterableEmpty(partition.getRejected());
    }

    @Override
    public void partitionWith()
    {
        PartitionImmutableBag<String> partition = this.newBag().partitionWith(Predicates2.<String>lessThan(), "0");
        Verify.assertIterableEmpty(partition.getSelected());
        Verify.assertIterableEmpty(partition.getRejected());
    }

    @Test
    public void selectInstancesOf()
    {
        ImmutableBag<Number> numbers = Bags.immutable.of();
        Assert.assertEquals(iBag(), numbers.selectInstancesOf(Integer.class));
        Assert.assertEquals(iBag(), numbers.selectInstancesOf(Double.class));
        Assert.assertEquals(iBag(), numbers.selectInstancesOf(Number.class));
    }

    @Override
    @Test
    public void testToString()
    {
        super.testToString();
        Assert.assertEquals("[]", this.newBag().toString());
    }

    @Override
    @Test
    public void testSize()
    {
        Verify.assertIterableSize(0, this.newBag());
    }

    @Override
    @Test
    public void newWithout()
    {
        Assert.assertSame(this.newBag(), this.newBag().newWithout("1"));
    }

    @Override
    public void toStringOfItemToCount()
    {
        Assert.assertEquals("", Bags.immutable.of().toStringOfItemToCount());
    }

    @Override
    @Test
    public void detect()
    {
        Assert.assertNull(this.newBag().detect(Predicates.equal("1")));
    }

    @Override
    @Test
    public void detectWith()
    {
        Assert.assertNull(this.newBag().detectWith(Predicates2.<String>greaterThan(), "3"));
    }

    @Override
    @Test
    public void detectWithIfNone()
    {
        Assert.assertEquals("Not Found", this.newBag().detectWithIfNone(Predicates2.equal(), "1", new PassThruFunction0<String>("Not Found")));
    }

    @Override
    public void detectIfNone()
    {
        super.detectIfNone();

        Assert.assertEquals("Not Found", this.newBag().detectIfNone(Predicates.equal("2"), new PassThruFunction0<String>("Not Found")));
    }

    @Override
    @Test
    public void allSatisfy()
    {
        ImmutableBag<String> strings = this.newBag();
        Assert.assertTrue(strings.allSatisfy(ERROR_THROWING_PREDICATE));
    }

    @Override
    @Test
    public void anySatisfy()
    {
        ImmutableBag<String> strings = this.newBag();
        Assert.assertFalse(strings.anySatisfy(ERROR_THROWING_PREDICATE));
    }

    @Override
    @Test
    public void noneSatisfy()
    {
        ImmutableBag<String> strings = this.newBag();
        Assert.assertTrue(strings.noneSatisfy(ERROR_THROWING_PREDICATE));
    }

    @Test
    public void allSatisfyWith()
    {
        ImmutableBag<String> strings = this.newBag();
        Assert.assertTrue(strings.allSatisfyWith(ERROR_THROWING_PREDICATE_2, Integer.class));
    }

    @Test
    public void anySatisfyWith()
    {
        ImmutableBag<String> strings = this.newBag();
        Assert.assertFalse(strings.anySatisfyWith(ERROR_THROWING_PREDICATE_2, Integer.class));
    }

    @Test
    public void noneSatisfyWith()
    {
        ImmutableBag<String> strings = this.newBag();
        Assert.assertTrue(strings.noneSatisfyWith(ERROR_THROWING_PREDICATE_2, Integer.class));
    }

    @Override
    @Test
    public void getFirst()
    {
        Assert.assertNull(this.newBag().getFirst());
    }

    @Override
    @Test
    public void getLast()
    {
        Assert.assertNull(this.newBag().getLast());
    }

    @Override
    @Test
    public void isEmpty()
    {
        ImmutableBag<String> bag = this.newBag();
        Assert.assertTrue(bag.isEmpty());
        Assert.assertFalse(bag.notEmpty());
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void min()
    {
        this.newBag().min(Comparators.naturalOrder());
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void max()
    {
        this.newBag().max(Comparators.naturalOrder());
    }

    @Test
    @Override
    public void min_null_throws()
    {
        super.min_null_throws();
    }

    @Test
    @Override
    public void max_null_throws()
    {
        super.max_null_throws();
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void min_without_comparator()
    {
        this.newBag().min();
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void max_without_comparator()
    {
        this.newBag().max();
    }

    @Test
    @Override
    public void min_null_throws_without_comparator()
    {
        // Not applicable for empty collections
        super.min_null_throws_without_comparator();
    }

    @Test
    @Override
    public void max_null_throws_without_comparator()
    {
        // Not applicable for empty collections
        super.max_null_throws_without_comparator();
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void minBy()
    {
        this.newBag().minBy(Functions.getToString());
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void maxBy()
    {
        this.newBag().maxBy(Functions.getToString());
    }

    @Override
    @Test
    public void zip()
    {
        ImmutableBag<String> immutableBag = this.newBag();
        List<Object> nulls = Collections.nCopies(immutableBag.size(), null);
        List<Object> nullsPlusOne = Collections.nCopies(immutableBag.size() + 1, null);

        ImmutableBag<Pair<String, Object>> pairs = immutableBag.zip(nulls);
        Assert.assertEquals(
                immutableBag,
                pairs.collect(Functions.<String>firstOfPair()));
        Assert.assertEquals(
                HashBag.newBag(nulls),
                pairs.collect(Functions.secondOfPair()));

        ImmutableBag<Pair<String, Object>> pairsPlusOne = immutableBag.zip(nullsPlusOne);
        Assert.assertEquals(
                immutableBag,
                pairsPlusOne.collect(Functions.<String>firstOfPair()));
        Assert.assertEquals(
                HashBag.newBag(nulls),
                pairsPlusOne.collect(Functions.secondOfPair()));

        Assert.assertEquals(immutableBag.zip(nulls), immutableBag.zip(nulls, HashBag.<Pair<String, Object>>newBag()));
    }

    @Override
    @Test
    public void zipWithIndex()
    {
        ImmutableBag<String> immutableBag = this.newBag();
        ImmutableSet<Pair<String, Integer>> pairs = immutableBag.zipWithIndex();

        Assert.assertEquals(UnifiedSet.<String>newSet(), pairs.collect(Functions.<String>firstOfPair()));
        Assert.assertEquals(UnifiedSet.<Integer>newSet(), pairs.collect(Functions.<Integer>secondOfPair()));

        Assert.assertEquals(immutableBag.zipWithIndex(), immutableBag.zipWithIndex(UnifiedSet.<Pair<String, Integer>>newSet()));
    }

    @Test
    public void chunk()
    {
        Assert.assertEquals(this.newBag(), this.newBag().chunk(2));
    }

    @Override
    @Test(expected = IllegalArgumentException.class)
    public void chunk_zero_throws()
    {
        this.newBag().chunk(0);
    }

    @Override
    @Test
    public void chunk_large_size()
    {
        Assert.assertEquals(this.newBag(), this.newBag().chunk(10));
        Verify.assertInstanceOf(ImmutableBag.class, this.newBag().chunk(10));
    }

    @Override
    @Test
    public void toSortedMap()
    {
        MutableSortedMap<String, String> map = this.newBag().toSortedMap(Functions.getStringPassThru(), Functions.getStringPassThru());
        Verify.assertEmpty(map);
        Verify.assertInstanceOf(TreeSortedMap.class, map);
    }

    @Override
    @Test
    public void toSortedMap_with_comparator()
    {
        MutableSortedMap<String, String> map = this.newBag().toSortedMap(Comparators.<String>reverseNaturalOrder(),
                Functions.getStringPassThru(), Functions.getStringPassThru());
        Verify.assertEmpty(map);
        Verify.assertInstanceOf(TreeSortedMap.class, map);
        Assert.assertEquals(Comparators.<String>reverseNaturalOrder(), map.comparator());
    }

    @Override
    @Test
    public void serialization()
    {
        ImmutableBag<String> bag = this.newBag();
        Verify.assertPostSerializedIdentity(bag);
    }

    @Override
    @Test
    public void collectBoolean()
    {
        ImmutableBooleanBag result = this.newBag().collectBoolean("4"::equals);
        Assert.assertEquals(0, result.sizeDistinct());
        Assert.assertEquals(0, result.occurrencesOf(true));
        Assert.assertEquals(0, result.occurrencesOf(false));
    }

    @Override
    @Test
    public void collectBooleanWithTarget()
    {
        BooleanHashBag target = new BooleanHashBag();
        BooleanHashBag result = this.newBag().collectBoolean("4"::equals, target);
        Assert.assertSame("Target sent as parameter not returned", target, result);
        Assert.assertEquals(0, result.sizeDistinct());
        Assert.assertEquals(0, result.occurrencesOf(true));
        Assert.assertEquals(0, result.occurrencesOf(false));
    }

    @Override
    @Test
    public void collect_target()
    {
        MutableList<Integer> targetCollection = FastList.newList();
        MutableList<Integer> actual = this.newBag().collect(object -> {
            throw new AssertionError();
        }, targetCollection);
        Assert.assertEquals(targetCollection, actual);
        Assert.assertSame(targetCollection, actual);
    }

    @Override
    @Test
    public void collectWith_target()
    {
        MutableList<Integer> targetCollection = FastList.newList();
        MutableList<Integer> actual = this.newBag().collectWith((argument1, argument2) -> {
            throw new AssertionError();
        }, 1, targetCollection);
        Assert.assertEquals(targetCollection, actual);
        Assert.assertSame(targetCollection, actual);
    }
}
