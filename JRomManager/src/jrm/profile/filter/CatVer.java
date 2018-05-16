package jrm.profile.filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import javax.swing.tree.TreeNode;

import org.apache.commons.lang3.StringUtils;

import jrm.profile.data.PropertyStub;
import jrm.ui.AbstractNGTreeNode;

public final class CatVer extends AbstractNGTreeNode implements Iterable<jrm.profile.filter.CatVer.Category>, PropertyStub
{
	private final Map<String, Category> categories = new TreeMap<>();
	private final List<Category> list_categories = new ArrayList<>();
	public final File file;

	public final static class Category extends AbstractNGTreeNode implements Map<String, SubCategory>, Iterable<SubCategory>, PropertyStub
	{
		public final String name;
		private final CatVer parent;
		private final Map<String, SubCategory> subcategories = new TreeMap<>();
		private final List<SubCategory> list_subcategories = new ArrayList<>();

		public Category(String name, CatVer parent)
		{
			this.name = name;
			this.parent = parent;
		}

		@Override
		public int size()
		{
			return subcategories.size();
		}

		@Override
		public boolean isEmpty()
		{
			return subcategories.isEmpty();
		}

		@Override
		public boolean containsKey(Object key)
		{
			return subcategories.containsKey(key);
		}

		@Override
		public boolean containsValue(Object value)
		{
			return subcategories.containsValue(value);
		}

		@Override
		public SubCategory get(Object key)
		{
			return subcategories.get(key);
		}

		@Override
		public SubCategory put(String key, SubCategory value)
		{
			return subcategories.put(key, value);
		}

		@Override
		public SubCategory remove(Object key)
		{
			return subcategories.remove(key);
		}

		@Override
		public void putAll(Map<? extends String, ? extends SubCategory> m)
		{
			subcategories.putAll(m);
		}

		@Override
		public void clear()
		{
			subcategories.clear();
		}

		@Override
		public Set<String> keySet()
		{
			return subcategories.keySet();
		}

		@Override
		public Collection<SubCategory> values()
		{
			return subcategories.values();
		}

		@Override
		public Set<Entry<String, SubCategory>> entrySet()
		{
			return subcategories.entrySet();
		}

		@Override
		public TreeNode getChildAt(int childIndex)
		{
			return list_subcategories.get(childIndex);
		}

		@Override
		public int getChildCount()
		{
			return list_subcategories.size();
		}

		@Override
		public TreeNode getParent()
		{
			return parent;
		}

		@Override
		public int getIndex(TreeNode node)
		{
			return list_subcategories.indexOf(node);
		}

		@Override
		public boolean getAllowsChildren()
		{
			return true;
		}

		@Override
		public boolean isLeaf()
		{
			return list_subcategories.size() == 0;
		}

		@Override
		public Enumeration<SubCategory> children()
		{
			return Collections.enumeration(list_subcategories);
		}

		@Override
		public Object getUserObject()
		{
			return String.format("%s (%d)", name, list_subcategories.stream().filter(SubCategory::isSelected).mapToInt(SubCategory::size).sum());
		}

		@Override
		public Iterator<SubCategory> iterator()
		{
			return list_subcategories.iterator();
		}

		@Override
		public String getPropertyName()
		{
			return "filter.cat." + name;
		}

		@Override
		public void setSelected(boolean selected)
		{
			PropertyStub.super.setSelected(selected);
		}

		@Override
		public boolean isSelected()
		{
			return PropertyStub.super.isSelected();
		}

	}

	public final static class SubCategory extends AbstractNGTreeNode implements List<String>, PropertyStub
	{
		public final String name;
		private final Category parent;
		private final List<String> games = new ArrayList<>();

		public SubCategory(String name, Category parent)
		{
			this.name = name;
			this.parent = parent;
		}

		@Override
		public Iterator<String> iterator()
		{
			return games.iterator();
		}

		@Override
		public int size()
		{
			return games.size();
		}

		@Override
		public boolean isEmpty()
		{
			return games.isEmpty();
		}

		@Override
		public boolean contains(Object o)
		{
			return games.contains(o);
		}

		@Override
		public Object[] toArray()
		{
			return games.toArray();
		}

		@Override
		public <T> T[] toArray(T[] a)
		{
			return games.toArray(a);
		}

		@Override
		public boolean add(String e)
		{
			return games.add(e);
		}

		@Override
		public boolean remove(Object o)
		{
			return games.remove(o);
		}

		@Override
		public boolean containsAll(Collection<?> c)
		{
			return games.containsAll(c);
		}

		@Override
		public boolean addAll(Collection<? extends String> c)
		{
			return games.addAll(c);
		}

		@Override
		public boolean addAll(int index, Collection<? extends String> c)
		{
			return games.addAll(index, c);
		}

		@Override
		public boolean removeAll(Collection<?> c)
		{
			return games.removeAll(c);
		}

		@Override
		public boolean retainAll(Collection<?> c)
		{
			return games.retainAll(c);
		}

		@Override
		public void clear()
		{
			games.clear();
		}

		@Override
		public String get(int index)
		{
			return games.get(index);
		}

		@Override
		public String set(int index, String element)
		{
			return games.set(index, element);
		}

		@Override
		public void add(int index, String element)
		{
			games.add(index, element);
		}

		@Override
		public String remove(int index)
		{
			return games.remove(index);
		}

		@Override
		public int indexOf(Object o)
		{
			return games.indexOf(o);
		}

		@Override
		public int lastIndexOf(Object o)
		{
			return games.lastIndexOf(o);
		}

		@Override
		public ListIterator<String> listIterator()
		{
			return games.listIterator();
		}

		@Override
		public ListIterator<String> listIterator(int index)
		{
			return games.listIterator(index);
		}

		@Override
		public List<String> subList(int fromIndex, int toIndex)
		{
			return games.subList(fromIndex, toIndex);
		}

		@Override
		public TreeNode getChildAt(int childIndex)
		{
			return null;
		}

		@Override
		public int getChildCount()
		{
			return 0;
		}

		@Override
		public TreeNode getParent()
		{
			return parent;
		}

		@Override
		public int getIndex(TreeNode node)
		{
			return 0;
		}

		@Override
		public boolean getAllowsChildren()
		{
			return false;
		}

		@Override
		public boolean isLeaf()
		{
			return true;
		}

		@Override
		public Enumeration<?> children()
		{
			return null;
		}

		@Override
		public Object getUserObject()
		{
			return name + " (" + games.size() + ")";
		}

		@Override
		public String getPropertyName()
		{
			return "filter.cat." + parent.name + "." + name;
		}

		@Override
		public void setSelected(boolean selected)
		{
			PropertyStub.super.setSelected(selected);
		}

		@Override
		public boolean isSelected()
		{
			return PropertyStub.super.isSelected();
		}
	}

	private CatVer(File file) throws IOException
	{
		try(BufferedReader reader = new BufferedReader(new FileReader(file));)
		{
			this.file = file;
			String line;
			boolean in_section = false;
			while(null != (line = reader.readLine()))
			{
				if(line.equalsIgnoreCase("[Category]"))
					in_section = true;
				else if(line.startsWith("[") && in_section)
					break;
				else if(in_section)
				{
					String[] kv = StringUtils.split(line, '=');
					if(kv.length == 2)
					{
						String k = kv[0].trim();
						String[] v = StringUtils.split(kv[1], '/');
						if(v.length == 2)
						{
							String c = v[0].trim();
							String sc = v[1].trim();
							Category cat;
							if(!categories.containsKey(c))
								categories.put(c, cat = new Category(c, CatVer.this));
							else
								cat = categories.get(c);
							SubCategory subcat;
							if(!cat.containsKey(sc))
								cat.put(sc, subcat = new SubCategory(sc, cat));
							else
								subcat = cat.get(sc);
							subcat.add(k);
						}
					}
				}
			}
			list_categories.addAll(categories.values());
			for(Category cat : list_categories)
				cat.list_subcategories.addAll(cat.subcategories.values());
			if(list_categories.isEmpty())
				throw new IOException("No CatVer data");
		}
	}

	public static CatVer read(File file) throws IOException
	{
		return new CatVer(file);
	}

	@Override
	public TreeNode getChildAt(int childIndex)
	{
		return list_categories.get(childIndex);
	}

	@Override
	public int getChildCount()
	{
		return list_categories.size();
	}

	@Override
	public TreeNode getParent()
	{
		return null;
	}

	@Override
	public int getIndex(TreeNode node)
	{
		return list_categories.indexOf(node);
	}

	@Override
	public boolean getAllowsChildren()
	{
		return true;
	}

	@Override
	public boolean isLeaf()
	{
		return list_categories.size() == 0;
	}

	@Override
	public Enumeration<Category> children()
	{
		return Collections.enumeration(list_categories);
	}

	@Override
	public Object getUserObject()
	{
		return String.format("%s (%d)", "All Categories", list_categories.stream().flatMap(c -> c.list_subcategories.stream().filter(SubCategory::isSelected)).mapToInt(SubCategory::size).sum());
	}

	@Override
	public Iterator<Category> iterator()
	{
		return list_categories.iterator();
	}

	@Override
	public String getPropertyName()
	{
		return "filter.cat";
	}

	@Override
	public void setSelected(boolean selected)
	{
		PropertyStub.super.setSelected(selected);
	}

	@Override
	public boolean isSelected()
	{
		return PropertyStub.super.isSelected();
	}

}